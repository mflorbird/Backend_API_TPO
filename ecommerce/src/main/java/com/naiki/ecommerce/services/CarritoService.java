import com.naiki.ecommerce.repository.CarritoRepository;
import com.naiki.ecommerce.repository.ItemCarritoRepository;
import com.naiki.ecommerce.repository.entity.Carrito;
import com.naiki.ecommerce.repository.entity.ItemCarrito;
import com.naiki.ecommerce.repository.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service // para que spring la pueda poner en otraspartes.
public class CarritoService{
    @Autowired // trae los repositorios
    private CarritoRepository carritoRepository;
    @Autowired
    private ItemCarritoRepository itemCarritoRepository;
    @Autowired
    private ProductoRepository productoRepository;

    @Transactional //si ocurre una excepcion durante la ejecucion , la transaccion se revierte. ATOMICIDAD.
    public Carrito agregarProductoAlCarrito(long carritoId, Producto producto, int cantidad) throws SinStockException{
        //para buscar el carrito por id
        //busca el carrito por ID en la BD usando carritoReposito.
        //devuelve el optional<carrito> que puede tener o no dato.
        Optional<Carrito> carritoOpt = carritoRepository.findById(carritoId);
        Carrito carrito;
        //si hay carro lo devuelve
        if (carritoOpt.isPresent()){
            carrito=carritoOpt.get();
            //si no existe lo crea.
        }else{
            carrito=new Carrito ();
        }
        //HAY QUE VERIFICAR STOCK
        if(producto.getStock()<cantidad){
            throw new SinStockException ("Stock insuficiente para producto:" + producto.getNombre());
        }

        //Agregar o actualizar el item del carrito
        boolean productoExistente = false;
        //busca si el producto ya existe en el carro
        //pra isso recorre la lista itemcarrito .
        for (ItemCarrito item: carrito.getItems()){
            //si existe aumenta cantidad
            if (item.getProducto().getId().equals(producto.getId())) {
                item.setCantidad(item.getCantidad() + cantidad);//si existe se actualiza la cant.
                productoExistente = true;
                break;
            }
        }
        //si el producto no existe crea el producto en el carro.
        if (!productoExistente){
            ItemCarrito newItem=new ItemCarrito(producto, cantidad);
            newItem.setCarrito(carrito);
            carrito.getItems().add(newItem);
        }

        carrito.recalcularTotal();
        return carritoRepository.save(carrito);
    }

    @Transactional
    public void eliminarProductoDelCarrito(Long carritoId, Long productoId) {
      //busca carro
        Optional<Carrito> carritoOpt = carritoRepository.findById(carritoId);
        if (carritoOpt.isPresent()) {
            Carrito carrito = carritoOpt.get();
            //si el carro existe remueve el producto
            carrito.getItems().removeIf(item->item.getProducto().getId().equals(productoId));
            //y recalcula el precio
            carrito.recalcularTotal();
            carritoRepository.save(carrito);
        }
    }

}

