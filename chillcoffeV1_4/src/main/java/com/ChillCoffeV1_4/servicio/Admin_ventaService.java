package com.ChillCoffeV1_4.servicio;

import com.ChillCoffeV1_4.modelo.Venta;
import com.ChillCoffeV1_4.repositorio.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Admin_ventaService {

    @Autowired
    private VentaRepository ventaRepository;

    public void guardarVenta(Venta venta) {
        ventaRepository.save(venta);
    }

    public List<Venta> obtenerTodas() {
        return ventaRepository.findAll();
    }

    // --- MÉTODO PARA ELIMINAR ---
    public void eliminarVentaPorId(Long id) {
        ventaRepository.deleteById(id);
    }
    
    // --- MÉTODO PARA BUSCAR ---
    public List<Venta> buscarVentas(String terminoBusqueda) {
        if (terminoBusqueda == null || terminoBusqueda.trim().isEmpty()) {
            return obtenerTodas(); // Si no hay término, devuelve todas
        }
        // Llama al método del repositorio para filtrar por producto
        // NOTA: ventaRepository DEBE tener el método findByProductoContainingIgnoreCase
        return ventaRepository.findByProductoContainingIgnoreCase(terminoBusqueda);
    }
}