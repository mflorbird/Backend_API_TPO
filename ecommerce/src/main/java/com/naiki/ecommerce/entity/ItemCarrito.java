import javax.persistence.*;
import com.naiki.ecommerce.repositoy.entity;

@Entity
public class ItemCarrito{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="carrito_id")
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name="producto_id")
    private Producto producto;

    private int cantidad;

    //Construcctor producto y cant
    public ItemCarrito(Producto producto, int cantidad){
        this.producto=producto;
        this.cantidad=cantidad;
    }

    //get y set
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

}