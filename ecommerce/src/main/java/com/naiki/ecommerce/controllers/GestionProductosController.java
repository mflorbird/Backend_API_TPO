package com.naiki.ecommerce.controllers;

import com.naiki.ecommerce.dto.ProductoDTO;
import com.naiki.ecommerce.repository.entity.Producto;
import com.naiki.ecommerce.service.GestionProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping ("api/v1/gestionProductos")
public class GestionProductosController {

    @Autowired
    private GestionProductosService gestionProductosService;

    @PostMapping ("/productos")
    public ResponseEntity<?> altaProducto(@RequestBody ProductoDTO productoDTO) {
        try{
            // Convertir ProductoDTO.Stock a Producto.Stock
            List<Producto.Stock> stockTotalEntity = productoDTO.getStockTotal().stream()
                    .map(stock -> new Producto.Stock(stock.getSize(), stock.getStock()))
                    .collect(Collectors.toList());
            gestionProductosService.altaProducto(
                    productoDTO.getId(),
                    productoDTO.getModel(),
                    productoDTO.getCategory(),
                    productoDTO.getDescription(),
                    productoDTO.getImage(),
                    productoDTO.isFeatured(),
                    productoDTO.getEstado(),
                    productoDTO.getPrice(),
                    stockTotalEntity
            );
//            gestionProductosService.altaProducto(  productoDTO.getNombre(),
//                    productoDTO.getCategoria(),
//                    productoDTO.getDescripcion(),
//                    productoDTO.getFoto(),
//                    productoDTO.isDestacado(),
//                    productoDTO.getEstado(),
//                    productoDTO.getPrecio(),
//                    productoDTO.getStock() );
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
//    //ESTE NO LO TOCO, PERO CREO QUE YA NO VA DE ESTA MANERA, PORQUE QUANTITY LO TENEMOS DENTRO DE STOCK.
//    @PutMapping("/productos/{productoId}/stock")
//    public ResponseEntity<?> modificarStockProducto(@RequestParam("productoId") Long productoId, @RequestParam("cantidad") int cantidad) {
//        try{
//            gestionProductosService.modificarStockProducto(productoId, cantidad);
//            return ResponseEntity.ok().build();
//        }catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
//        }
//    }

    //AGREGO ESTE PARA MODIFICAR STOCK PORQUE EN FRONT LO TENEMOS DENTRO DE UNA LISTA. PERO PUEDE SER QUE NECESITEMOS ACA AGREGARLE SIZE Y STOCK.
    @PutMapping("/productos/{productoId}/stock")
    public ResponseEntity<?> modificarStockProducto(@PathVariable("productoId") Long productoId, @RequestBody List<ProductoDTO.Stock> stockTotal) {
        try {
            // Convertir ProductoDTO.Stock a Producto.Stock
            List<Producto.Stock> stockTotalEntity = stockTotal.stream()
                    .map(stock -> new Producto.Stock())
                    .toList();
            gestionProductosService.modificarStockProducto(productoId, stockTotalEntity);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    //ESTE LO DEJO COMO ESTABA, PERO ENTIENDO QUE HAY QUE MODIFICARLO POR LO QUE AGREGO EN LA LINEA 65
    @PutMapping("/productos/{productoId}/destacado")
    public ResponseEntity<?> modificarDestacadoProducto(@RequestParam("productoId") Long productoId, @RequestParam("destacado") boolean destacado) {
        try{
            gestionProductosService.modificarDestacadoProducto(productoId, destacado);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // AGREGO ESTO 20/11
    @PutMapping("/productos/{productoId}/featured")
    public ResponseEntity<?> modificarFeaturedProducto(@PathVariable("productoId") Long productoId, @RequestParam("featured") boolean featured) {
        try {
            gestionProductosService.modificarFeaturedProducto(productoId, featured);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping ("/productos/{productoId}")
    public void bajaProducto(@PathVariable("productoId") Long productoId){
        try{
            gestionProductosService.bajaProducto(productoId);
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


}
