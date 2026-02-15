package com.ChillCoffeV1_4.servicio;

import com.ChillCoffeV1_4.modelo.ItemCarrito;
import com.ChillCoffeV1_4.modelo.Producto;
import com.ChillCoffeV1_4.modelo.Venta;
import com.ChillCoffeV1_4.repositorio.VentaRepository;
import com.ChillCoffeV1_4.repositorio.ProductoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // ⭐ NUEVO/CORREGIDO: Necesario para el método de abajo

@Service
public class Clien_comprasService {

    private static final String CARRO_SESION = "carroDeCompras";

    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository; 
    
    public Clien_comprasService(ProductoRepository productoRepository, VentaRepository ventaRepository) {
        this.productoRepository = productoRepository;
        this.ventaRepository = ventaRepository; 
    }

    /**
     * ⭐ MÉTODO FALTANTE AÑADIDO: Devuelve una lista de todos los productos disponibles (stock > 0).
     */
    public List<Producto> obtenerProductosDisponibles() {
        return productoRepository.findAll().stream()
                .filter(p -> p.getStock() != null && p.getStock() > 0)
                .collect(Collectors.toList());
    }

    public void eliminarProductoDeCarrito(HttpSession session, Long productoId) {
        List<ItemCarrito> carrito = obtenerCarrito(session);
        carrito.removeIf(item -> item.getId().equals(productoId)); 
        session.setAttribute(CARRO_SESION, carrito);
    }
    
    @SuppressWarnings("unchecked")
    public List<ItemCarrito> obtenerCarrito(HttpSession session) {
        Object carritoObj = session.getAttribute(CARRO_SESION);
        
        if (carritoObj == null) {
            List<ItemCarrito> nuevoCarrito = new ArrayList<>();
            session.setAttribute(CARRO_SESION, nuevoCarrito);
            return nuevoCarrito;
        }
        
        return (List<ItemCarrito>) carritoObj; 
    }
    
    public void limpiarCarrito(HttpSession session) {
        session.removeAttribute(CARRO_SESION);
    }
    
    public Double calcularTotal(HttpSession session) {
        List<ItemCarrito> carrito = obtenerCarrito(session);
        return carrito.stream()
                .mapToDouble(item -> item.getPrecio() * item.getCantidad())
                .sum();
    }
    
    public boolean agregarProductoACarrito(HttpSession session, Long productoId, int cantidad) {
        if (cantidad <= 0) return false;

        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        if (productoOpt.isEmpty()) return false;

        Producto producto = productoOpt.get();
        List<ItemCarrito> carrito = obtenerCarrito(session);

        Optional<ItemCarrito> itemExistenteOpt = carrito.stream()
                .filter(item -> item.getId().equals(productoId)) 
                .findFirst();

        int stockActual = producto.getStock() != null ? producto.getStock() : 0;
        
        if (itemExistenteOpt.isPresent()) {
            ItemCarrito item = itemExistenteOpt.get();
            int cantidadTotal = item.getCantidad() + cantidad;
            
            if (cantidadTotal > stockActual) {
                return false;
            }
            item.setCantidad(cantidadTotal);
        } else {
            if (cantidad > stockActual) {
                return false;
            }
            // El precio de Producto.getPrecio() ya debe ser Double
            ItemCarrito nuevoItem = new ItemCarrito(
                productoId, 
                producto.getNombre(), 
                producto.getPrecio(), 
                cantidad
            );
            carrito.add(nuevoItem);
        }
        
        session.setAttribute(CARRO_SESION, carrito);
        return true;
    }


    /**
     * Procesa la compra: Descuenta stock y REGISTRA LA VENTA.
     */
    @Transactional 
    public boolean procesarCheckout(HttpSession session) {
        List<ItemCarrito> carrito = obtenerCarrito(session);
        
        if (carrito.isEmpty()) {
            return false;
        }

        String nombreCliente = (String) session.getAttribute("nombre");
        if (nombreCliente == null) {
            nombreCliente = "Cliente Anónimo";
        }

        // 1. Verificar y Descontar Stock & 2. Registrar Venta
        for (ItemCarrito item : carrito) {
            Optional<Producto> productoOpt = productoRepository.findById(item.getId());
            
            if (productoOpt.isEmpty()) {
                throw new RuntimeException("Producto con ID " + item.getId() + " no encontrado durante el checkout.");
            }
            
            Producto producto = productoOpt.get();
            int stockActual = producto.getStock() != null ? producto.getStock() : 0;
            
            if (stockActual < item.getCantidad()) {
                return false; 
            }
            
            // Lógica 1: Descuento de Stock
            producto.setStock(stockActual - item.getCantidad());
            productoRepository.save(producto);
            
            // Lógica 2: Registro de Venta
            Double precioUnitarioDouble = item.getPrecio(); 
            
            Venta nuevaVenta = new Venta(
                item.getNombre(),           
                item.getCantidad(),         
                precioUnitarioDouble,       // Usamos Double
                nombreCliente,              
                "CLIENTE"                   
            );
            
            ventaRepository.save(nuevaVenta); 
        }

        // 3. Limpiar el carrito 
        limpiarCarrito(session);
        
        return true;
    }
}