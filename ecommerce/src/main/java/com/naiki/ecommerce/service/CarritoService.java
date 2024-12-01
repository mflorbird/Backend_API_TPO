package com.naiki.ecommerce.service;

import com.naiki.ecommerce.controllers.carrito.CheckoutResponse;
import com.naiki.ecommerce.dto.CarritoRequest;
import com.naiki.ecommerce.repository.CarritoRepository;
import com.naiki.ecommerce.repository.ItemCarritoRepository;
import com.naiki.ecommerce.repository.ProductoRepository;
import com.naiki.ecommerce.repository.UserRepository;
import com.naiki.ecommerce.repository.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

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

    public Carrito obtenerCarritoUsuario(String email) {
        long usuarioId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();
        List<Carrito> carritos = carritoRepository.findByUserId(usuarioId);
        if (carritos.isEmpty()) {
            return null;
        } else {
            // buscar carrito con estado activo
            for (Carrito carrito : carritos) {
                if ("activo".equalsIgnoreCase(carrito.getEstado())) {
                    return carrito;
                }
            }
            return null;
        }
    }


    public Carrito createCarrito(String email) {
        Carrito carrito = new Carrito();
        Long userId = userRepository.findByEmail(email).orElse(null).getId();
        carrito.setUserId(userId);
        carritoRepository.save(carrito);
        return carrito;
    }


    @Transactional(rollbackFor = Exception.class)
    public Carrito updateCarrito(Long carritoId, CarritoRequest carritoRequest) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        System.out.println("Carrito encontrado" + carrito);
        System.out.println("CarritoRequest" + carritoRequest);
        if (carritoRequest.getPrecioTotal() != null) {
            carrito.setPrecioTotal(carritoRequest.getPrecioTotal());
        }
        if (carritoRequest.getPrecioDiscount() != null) {
            carrito.setPrecioDiscount(carritoRequest.getPrecioDiscount());
        }
        if (carritoRequest.getDiscount() != null) {
            carrito.setDiscount(carritoRequest.getDiscount());
        }
        if (carritoRequest.getEstado() != null) {
            carrito.setEstado(carritoRequest.getEstado());
        }
        if (carritoRequest.getClosedAt() != null) {
            carrito.setClosedAt(carritoRequest.getClosedAt());
        }
        if (carritoRequest.getItems() != null) {
            carrito.setItems(carritoRequest.getItems());
        }
        System.out.println("Carrito actualizado" + carrito);
        carritoRepository.save(carrito);
        return carrito;
    }

    public Carrito deleteItem(String itemId, Carrito carrito) {
        itemCarritoRepository.eliminarPorItemKeyAndCarritoId(itemId, carrito.getId());
        Carrito carritoActualizado = carritoRepository.findById(carrito.getId()).orElse(null);
        return carritoActualizado;
    }

    public Carrito emptyCart(Long cartId, Carrito carrito) {
        itemCarritoRepository.eliminarPorCarritoId(cartId);
        carrito.setPrecioTotal(0.0);
        carrito.setDiscount(0.0);
        carrito.setPrecioDiscount(0.0);
        carrito.setItems(null);
        carritoRepository.save(carrito);
        Carrito carritoActualizado = carritoRepository.findById(carrito.getId()).orElse(null);
        return carritoActualizado;
    }

    @Transactional(rollbackFor = Exception.class)
    public CheckoutResponse realizarCheckout(Carrito carrito) {



        List<CheckoutResponse.InvalidItem> invalidItems = new ArrayList<>();
        Map<String, CheckoutResponse.Validation> validations = new HashMap<>();

        // Validación de stock
        for (String itemKey : carrito.getItems().keySet()) {
            String[] keyParts = itemKey.split("---");
            String productoId = keyParts[0];
            String talle = keyParts[1];
            System.out.println("Producto ID: " + productoId);
            System.out.println("Talle: " + talle);
            int cantidadSolicitada = carrito.getItems().get(itemKey).getCantidad();

            System.out.println("Cantidad solicitada: " + cantidadSolicitada);
            Optional<Producto> producto = productoRepository.findById(Long.valueOf(productoId));
            if (producto.isEmpty()) {
                invalidItems.add(new CheckoutResponse.InvalidItem("Producto no encontrado (ID: " + productoId + ")", talle));
                validations.put(itemKey, new CheckoutResponse.Validation(false, cantidadSolicitada, 0));
                continue;
            }

            Producto productoEncontrado = producto.get();
            System.out.println("Producto encontrado: " + productoEncontrado);
            boolean stockValido = false;

            for (SizeStock sizeStock : productoEncontrado.getStockTotal()) {
                if (sizeStock.getTalle().equals(talle)) {
                    boolean stockDisponible = sizeStock.getCantidad() >= cantidadSolicitada;
                    System.out.println("Stock disponible: " + stockDisponible);

                    validations.put(
                            itemKey,
                            new CheckoutResponse.Validation(stockDisponible, cantidadSolicitada, sizeStock.getCantidad())
                    );

                    if (!stockDisponible) {
                        invalidItems.add(new CheckoutResponse.InvalidItem(productoEncontrado.getNombre(), talle));
                    } else {
                        stockValido = true;
                    }
                }
            }

            if (!stockValido) {
                return new CheckoutResponse(
                        false,
                        "No hay suficiente stock para algunos productos",
                        invalidItems,
                        validations
                );
            }
        }

        // Actualización del stock y cierre del carrito
        for (String itemKey : carrito.getItems().keySet()) {
            String[] keyParts = itemKey.split("---");
            String productoId = keyParts[0];
            String talle = keyParts[1];
            int cantidadSolicitada = carrito.getItems().get(itemKey).getCantidad();

            Producto productoEncontrado = productoRepository.findById(Long.valueOf(productoId)).get();

            for (SizeStock sizeStock : productoEncontrado.getStockTotal()) {
                if (sizeStock.getTalle().equals(talle)) {
                    sizeStock.setCantidad(sizeStock.getCantidad() - cantidadSolicitada);
                    System.out.println("Stock actualizado: " + sizeStock.getCantidad());
                    productoRepository.save(productoEncontrado);
                    System.out.println("Producto actualizado: " + productoEncontrado);
                    break;
                }
            }
        }
        System.out.println("Carrito por cerrar: " + carrito);
        carrito.setEstado("cerrado");
        carrito.setClosedAt(LocalDateTime.now());
        System.out.println("Carrito por cerrar 2: " + carrito);
        carritoRepository.save(carrito);
        System.out.println("Carrito cerrado: " + carrito);

        return new CheckoutResponse(
                true,
                "Checkout realizado con éxito",
                Collections.emptyList(),
                validations
        );
    }

    public List<Carrito> obtenerCarritosCerrados(String email) {
        long usuarioId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();
        return carritoRepository.findByUserIdAndEstado(usuarioId, "cerrado");
    }
}






//    @Transactional(rollbackFor = Exception.class) // ATOMICA
//    public Carrito agregarProductoAlCarrito(Long productoId, int cantidad) throws SinStockException {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new RuntimeException("Usuario no autenticado");
//        }
//        String email = authentication.getName();
//
//        //buscar usuario x mail. para obtener el id y no ingresarlo
//        long usuarioId = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
//                .getId();
//
//        //buscar el producto en la base, si no existe devuelve null [aca cambiar null por excepcion]
//        Producto producto = productoRepository.findById(productoId).orElse(null);
//        if (producto == null) {
//            throw new RuntimeException("Producto no encontrado");
//        }
//
//        // ahora ver si hay stock
//        if (producto.getStock() < cantidad) {
//            throw new SinStockException("No hay suficiente stock para el producto seleccionado");
//        }
//
//        //buscar carrito del usuario. si no lo encuentra lo crea.
//        List<Carrito> carritos = carritoRepository.findByUsuarioId(usuarioId);
//        Carrito carrito;
//
//        //si el usuario no tiene carrito, crearlo
//        if (carritos.isEmpty()) {
//            carrito = createCarrito();
//        } else {
//            carrito = carritos.get(0); //el usuario solo tiene un carrito, asi que obtenemos el primero
//        }
//
//        //ver si el producto ya esta en el carrito
//        //Agregar o actualizar el item del carrito
//        for (ItemCarrito item : carrito.getItems()) {
//            if (item.getProducto().getId() == productoId) {
//                item.setCantidad(item.getCantidad() + cantidad);
//                carrito.recalcularTotal();
//                carritoRepository.save(carrito);
//                return carrito;
//            }
//        }
//        ItemCarrito newItem = new ItemCarrito(producto, cantidad);
//        newItem.setCarrito(carrito);
//        carrito.getItems().add(newItem);
//        carrito.recalcularTotal();
//        carritoRepository.save(carrito);
//        return carrito;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public boolean eliminarProductoDelCarrito(Long carritoId, Long productoId, int cantidad) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new RuntimeException("Usuario no autenticado");
//        }
//
//        //busca producto
//        Producto producto = productoRepository.findById(productoId).orElse(null);
//        if (producto == null) {
//            return false;
//        }
//        //modificar stock
//        producto.setStock(producto.getStock() + cantidad);
//        productoRepository.save(producto);
//
//        //busca carro
//        Carrito carrito = carritoRepository.findById(carritoId).orElse(null);
//        if (carrito == null) {
//            return false;
//        }
//        //busca item
//        for (ItemCarrito item : carrito.getItems()) {
//            if (item.getProducto().getId() == productoId) {
//                item.setCantidad(item.getCantidad() - cantidad);
//                if (item.getCantidad() <= 0) {
//                    carrito.getItems().remove(item);
//                }
//                carrito.recalcularTotal();
//                carritoRepository.save(carrito);
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public boolean vaciarCarrito(Long carritoId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new RuntimeException("Usuario no autenticado");
//        }
//
//        Carrito carrito = carritoRepository.findById(carritoId).orElse(null);
//        if (carrito == null) {
//            return false;
//        }
//
//        for (ItemCarrito item : carrito.getItems()) {
//            Producto producto = item.getProducto();
//            producto.setStock(producto.getStock() + item.getCantidad());
//            productoRepository.save(producto);
//        }
//
//        carrito.vaciarCarrito();
//        carritoRepository.save(carrito);
//        return true;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public void realizarCheckout() throws SinStockException {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new RuntimeException("Usuario no autenticado");
//        }
//        String email = authentication.getName();
//        //obtener el usuario x mail
//        long usuarioId = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
//                .getId();
//        //buscar el carrito activo
//        List<Carrito> carritos = carritoRepository.findByUsuarioId(usuarioId);
//        if (carritos.isEmpty()) {
//            throw new RuntimeException("No tienes un carrito activo");
//        }
//
//        Carrito carrito = carritos.get(0);
//
//        //verificar stock de los prod. que tiene el carrito
//        for (ItemCarrito item : carrito.getItems()) {
//            Producto producto = item.getProducto();
//            if (producto.getStock() < item.getCantidad()) {
//                throw new SinStockException("No hay suficiente stock para el producto: " + producto.getNombre());
//            }
//        }
//
//        //Generar la baja del stock
//        for (ItemCarrito item : carrito.getItems()) {
//            Producto producto = item.getProducto();
//            producto.setStock(producto.getStock() - item.getCantidad());
//            productoRepository.save(producto);
//        }
//
//        //vaciar carrito
//        carrito.vaciarCarrito();
//        carritoRepository.save(carrito);
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public Carrito aplicarDescuentoAlCarrito(String codigoDescuento){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new RuntimeException("Usuario no autenticado");
//        }
//        String email = authentication.getName();
//
//        //validar codigo
//        if (!"NAIKI10".equalsIgnoreCase(codigoDescuento)){
//            throw new IllegalArgumentException("Código de descuento invalido");
//        }
//        //buscar carrito de usuario
//        long usuarioId = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
//                .getId();
//
//        List<Carrito> carritos = carritoRepository.findByUsuarioId(usuarioId);
//        if(carritos.isEmpty()){
//            throw new RuntimeException("No tienes un carrito activo");
//        }
//
//        Carrito carrito = carritos.get(0);
//        carrito.aplicarDescuento(codigoDescuento, 0.10); // aca reutilizo y aplico el 10%
//        carritoRepository.save(carrito);
//
//        return carrito;
//    }
//
//    public List<Carrito> obtenerCarritosPorUsuario(Long usuarioId) {
//        return carritoRepository.findByUsuarioId(usuarioId);
//    }
//
//    public Carrito obtenerCarritoUsuario(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new RuntimeException("Usuario no autenticado");
//        }
//        String email = authentication.getName();
//
//        //buscar el carrito del usuario
//        long usuarioId = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
//                .getId();
//
//        //buscar carrito del usuario.
//        List<Carrito> carritos = carritoRepository.findByUsuarioId(usuarioId);
//
//        //si el carrito no existe devuelve null (aca sera una pantalla que diga "no hay productos en carro")
//        if (carritos.isEmpty()){
//            return null;
//        }
//
//        //tomar el primer carrito de la lista del usuario, igual solo tiene 1
//        Carrito carrito = carritos.get(0);
//        //ver si esta vacio
//        if (carrito.getItems() == null || carrito.getItems().isEmpty()){
//            return carrito; //aca lo devolveria vacio
//        }
//        //y aca si tiene productos, lo devuelve con lo que tiene
//        return carrito;
//    }