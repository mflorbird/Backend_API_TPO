package com.naiki.ecommerce.service;

import com.naiki.ecommerce.exception.SinStockException;
import com.naiki.ecommerce.repository.CarritoRepository;
import com.naiki.ecommerce.repository.ItemCarritoRepository;
import com.naiki.ecommerce.repository.ProductoRepository;
import com.naiki.ecommerce.repository.UserRepository;
import com.naiki.ecommerce.repository.entity.Carrito;
import com.naiki.ecommerce.repository.entity.ItemCarrito;
import com.naiki.ecommerce.repository.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service // para que spring la pueda poner en otras partes.
public class CarritoService {
    @Autowired // trae los repositorios
    private CarritoRepository carritoRepository;

    public Carrito save(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Carrito> findAll() {
        return carritoRepository.findAll();
    }

    public Carrito findById(Long id) {
        return carritoRepository.findById(id).orElse(null);
    }

    public Carrito createCarrito() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }
        String email = authentication.getName();
        Carrito carrito = new Carrito();
        carrito.setUsuario(userRepository.findByEmail(email).orElse(null));
        carritoRepository.save(carrito);
        return carrito;
    }

    @Transactional(rollbackFor = Exception.class) // ATOMICA
    public Carrito agregarProductoAlCarrito(Long productoId, int cantidad) throws SinStockException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }
        String email = authentication.getName();

        //buscar usuario x mail. para obtener el id y no ingresarlo
        long usuarioId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();

        //buscar el producto en la base, si no existe devuelve null [aca cambiar null por excepcion]
        Producto producto = productoRepository.findById(productoId).orElse(null);
        if (producto == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        // ahora ver si hay stock
        if (producto.getStock() < cantidad) {
            throw new SinStockException("No hay suficiente stock para el producto seleccionado");
        }

        //buscar carrito del usuario. si no lo encuentra lo crea.
        List<Carrito> carritos = carritoRepository.findByUsuarioId(usuarioId);
        Carrito carrito;

        //si el usuario no tiene carrito, crearlo
        if (carritos.isEmpty()) {
            carrito = createCarrito();
        } else {
            carrito = carritos.get(0); //el usuario solo tiene un carrito, asi que obtenemos el primero
        }

        //ver si el producto ya esta en el carrito
        //Agregar o actualizar el item del carrito
        for (ItemCarrito item : carrito.getItems()) {
            if (item.getProducto().getId() == productoId) {
                item.setCantidad(item.getCantidad() + cantidad);
                carrito.recalcularTotal();
                carritoRepository.save(carrito);
                return carrito;
            }
        }
        ItemCarrito newItem = new ItemCarrito(producto, cantidad);
        newItem.setCarrito(carrito);
        carrito.getItems().add(newItem);
        carrito.recalcularTotal();
        carritoRepository.save(carrito);
        return carrito;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean eliminarProductoDelCarrito(Long carritoId, Long productoId, int cantidad) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }

        //busca producto
        Producto producto = productoRepository.findById(productoId).orElse(null);
        if (producto == null) {
            return false;
        }
        //modificar stock
        producto.setStock(producto.getStock() + cantidad);
        productoRepository.save(producto);

        //busca carro
        Carrito carrito = carritoRepository.findById(carritoId).orElse(null);
        if (carrito == null) {
            return false;
        }
        //busca item
        for (ItemCarrito item : carrito.getItems()) {
            if (item.getProducto().getId() == productoId) {
                item.setCantidad(item.getCantidad() - cantidad);
                if (item.getCantidad() <= 0) {
                    carrito.getItems().remove(item);
                }
                carrito.recalcularTotal();
                carritoRepository.save(carrito);
                return true;
            }
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean vaciarCarrito(Long carritoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }

        Carrito carrito = carritoRepository.findById(carritoId).orElse(null);
        if (carrito == null) {
            return false;
        }

        for (ItemCarrito item : carrito.getItems()) {
            Producto producto = item.getProducto();
            producto.setStock(producto.getStock() + item.getCantidad());
            productoRepository.save(producto);
        }

        carrito.vaciarCarrito();
        carritoRepository.save(carrito);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public void realizarCheckout() throws SinStockException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }
        String email = authentication.getName();
        //obtener el usuario x mail
        long usuarioId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();
        //buscar el carrito activo
        List<Carrito> carritos = carritoRepository.findByUsuarioId(usuarioId);
        if (carritos.isEmpty()) {
            throw new RuntimeException("No tienes un carrito activo");
        }

        Carrito carrito = carritos.get(0);

        //verificar stock de los prod. que tiene el carrito
        for (ItemCarrito item : carrito.getItems()) {
            Producto producto = item.getProducto();
            if (producto.getStock() < item.getCantidad()) {
                throw new SinStockException("No hay suficiente stock para el producto: " + producto.getNombre());
            }
        }

        //Generar la baja del stock
        for (ItemCarrito item : carrito.getItems()) {
            Producto producto = item.getProducto();
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);
        }

        //vaciar carrito
        carrito.vaciarCarrito();
        carritoRepository.save(carrito);
    }

    @Transactional(rollbackFor = Exception.class)
    public Carrito aplicarDescuentoAlCarrito(String codigoDescuento){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }
        String email = authentication.getName();

        //validar codigo
        if (!"NAIKI10".equalsIgnoreCase(codigoDescuento)){
            throw new IllegalArgumentException("Código de descuento invalido");
        }
        //buscar carrito de usuario
        long usuarioId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();

        List<Carrito> carritos = carritoRepository.findByUsuarioId(usuarioId);
        if(carritos.isEmpty()){
            throw new RuntimeException("No tienes un carrito activo");
        }

        Carrito carrito = carritos.get(0);
        carrito.aplicarDescuento(codigoDescuento, 0.10); // aca reutilizo y aplico el 10%
        carritoRepository.save(carrito);

        return carrito;
    }

    public List<Carrito> obtenerCarritosPorUsuario(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId);
    }

    public Carrito obtenerCarritoUsuario(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }
        String email = authentication.getName();

        //buscar el carrito del usuario
        long usuarioId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();

        //buscar carrito del usuario.
        List<Carrito> carritos = carritoRepository.findByUsuarioId(usuarioId);

        //si el carrito no existe devuelve null (aca sera una pantalla que diga "no hay productos en carro")
        if (carritos.isEmpty()){
            return null;
        }

        //tomar el primer carrito de la lista del usuario, igual solo tiene 1
        Carrito carrito = carritos.get(0);
        //ver si esta vacio
        if (carrito.getItems() == null || carrito.getItems().isEmpty()){
            return carrito; //aca lo devolveria vacio
        }
        //y aca si tiene productos, lo devuelve con lo que tiene
        return carrito;
    }
}