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
public class CarritoService{
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


    @Transactional //si ocurre una excepcion durante la ejecucion , la transaccion se revierte. ATOMICIDAD.
    public Carrito agregarProductoAlCarrito(Long carritoId, Long productoId, int cantidad) throws SinStockException {
        //para buscar el carrito por id
        //busca el carrito por ID en la BD usando carritoReposito.
        //devuelve el optional<carrito> que puede tener o no dato.
        Carrito carrito = carritoRepository.findById(carritoId).orElse(null);
        if (carrito == null) {
            return null;
        }
        Producto producto = productoRepository.findById(productoId).orElse(null);
        if (producto == null) {
            return null;
        }

        //verificar si hay stock
        if (producto.getStock() < cantidad)  {
            throw new SinStockException("No hay suficiente stock para el producto");
        }

        //modificar stock
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);

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
    public List<Carrito> obtenerCarritosPorUsuario(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId);
    }

}

