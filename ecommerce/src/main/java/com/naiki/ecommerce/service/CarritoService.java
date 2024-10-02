package com.naiki.ecommerce.service;
import com.naiki.ecommerce.controllers.config.JwtService;
import com.naiki.ecommerce.exception.SinStockException;
import com.naiki.ecommerce.repository.CarritoRepository;
import com.naiki.ecommerce.repository.ItemCarritoRepository;
import com.naiki.ecommerce.repository.ProductoRepository;
import com.naiki.ecommerce.repository.UserRepository;
import com.naiki.ecommerce.repository.entity.Carrito;
import com.naiki.ecommerce.repository.entity.ItemCarrito;
import com.naiki.ecommerce.repository.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

//aca tiene que estar revisar el checkout . verificar que tenga stock

@Service // para que spring la pueda poner en otraspartes.
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
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    public List<Carrito> findAll() {
        return carritoRepository.findAll();
    }

    public Carrito findById(Long id) {
        return carritoRepository.findById(id).orElse(null);
    }

    public Carrito createCarrito(String token) {
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        String email = jwtService.extractUsername(jwt);
        Carrito carrito = new Carrito();
        carrito.setUsuario(userRepository.findByEmail(email).orElse(null));
        carritoRepository.save(carrito);
        return carrito;
    }

    @Transactional // ATOMICA
    public Carrito agregarProductoAlCarrito(String token, Long productoId, int cantidad) throws SinStockException {
        String jwt = token.startsWith("Bearer ") ? token.substring(7): token;
        String email = jwtService.extractUsername(jwt);

        //buscar usuario x mail. para obtener el id y no ingresarlo
        long usuarioId = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado"))
                .getId();


        //buscar el producto en la base, si no existe devuelve null [aca cambiar null por excepcion]
        Producto producto = productoRepository.findById(productoId).orElse(null);
        if (producto == null){
            throw new RuntimeException("Producto no encontrado");
        }

        // ahora ver si hay stock
        if(producto.getStock()<cantidad){
            throw new SinStockException("No hay suficiente stock para el producto seleccionado");
        }

        //buscar carrito del usuario. si no lo encuentra lo crea.
        List<Carrito> carritos = carritoRepository.findByUsuarioId(usuarioId);
        Carrito carrito;

        //si el usuario no tiene carrito, crearlo
        if (carritos.isEmpty()) {
            carrito = createCarrito(token);
        }else {
            carrito = carritos.get(0); //el usuario solo tiene un carrito, asi que obtenemos el primero
        }


        //ver si el producto ya esta en el carrito
        //Agregar o actualizar el item del carrito
        for (ItemCarrito item: carrito.getItems()){
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


    @Transactional
    public boolean eliminarProductoDelCarrito(Long carritoId, Long productoId, int cantidad) {
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
        for (ItemCarrito item: carrito.getItems()) {
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

    @Transactional
    public boolean vaciarCarrito(Long carritoId) {
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

    public void realizarCheckout(Long carritoId, String token) {
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        String email = jwtService.extractUsername(jwt);

        Carrito carrito = carritoRepository.findById(carritoId).orElseThrow(() -> new RuntimeException("Carrito no encontrado."));

        if (!carrito.getUsuario().getEmail().equals(email)) {
            throw new RuntimeException("Este carrito no pertenece al usuario autenticado.");
        }

        carrito.setEstado("cerrado");

        carritoRepository.save(carrito);
    }



    public List<Carrito> obtenerCarritosPorUsuario(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId);
    }


    public Carrito obtenerCarritoUsuario(String token){
        String jwt = token.startsWith("Bearer ") ? token.substring(7): token;
        String email = jwtService.extractUsername(jwt);

        //buscar el carrito del uusario . pero primero
        //buscar usuario x mail. para obtener el id y no ingresarlo
        long usuarioId = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado"))
                .getId();

        //buscar carrito del usuario.
        List<Carrito> carritos = carritoRepository.findByUsuarioId(usuarioId);

        //si el carrito no existe devuelve null (aca sera una pantalla que diga "no hay productos en carro")
        if (carritos.isEmpty()){
            return null;
        }

        //tomar el primer carrito de la lista del usuario, igual solo tiene 1. pero lo tengo q inicializar
        Carrito carrito = carritos.get(0);
        //ver si esta vacio
        if (carrito.getItems() == null || carrito.getItems().isEmpty()){
            return carrito; //aca lo devolveria vacio
        }
        //y aca si tiene product. lo devuelve con lo que tiene
        return carrito;
    }
}
