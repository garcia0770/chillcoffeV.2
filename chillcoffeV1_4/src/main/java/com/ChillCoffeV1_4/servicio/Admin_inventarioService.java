package com.ChillCoffeV1_4.servicio;

import com.ChillCoffeV1_4.modelo.Producto;
import com.ChillCoffeV1_4.repositorio.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Admin_inventarioService { 

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }

    // ⭐ CORRECCIÓN: Ahora devuelve Double ⭐
    public Double obtenerPrecioPorNombre(String nombre) {
        Optional<Producto> opt = productoRepository.findByNombre(nombre);
        // Devuelve 0.0 si no se encuentra
        return opt.map(Producto::getPrecio).orElse(0.0); 
    }

    // 2. DESCONTAR STOCK (Se mantiene igual)
    public boolean descontarStockPorNombre(String nombre, int cantidad) {
        Optional<Producto> opt = productoRepository.findByNombre(nombre);
        
        if (opt.isPresent()) {
            Producto p = opt.get();
            if (p.getStock() != null && p.getStock() >= cantidad) {
                p.setStock(p.getStock() - cantidad);
                productoRepository.save(p);
                return true;
            }
        }
        return false;
    }

    // Agrega o modifica un producto (Admin)
    // ⭐ Se usa Double para el precio ⭐
    public void guardarProducto(String nombre, Integer stock, Double precio) {
        Optional<Producto> opt = productoRepository.findByNombre(nombre);
        Producto productoExistente;
        
        if (opt.isPresent()) {
            productoExistente = opt.get();
        } else {
            productoExistente = new Producto();
            productoExistente.setNombre(nombre);
        }
        productoExistente.setStock(stock);
        productoExistente.setPrecio(precio);
        productoRepository.save(productoExistente);
    }
    
    // Actualiza solo el stock (Se mantiene igual)
    public boolean actualizarSoloStock(String nombre, Integer stock) {
        Optional<Producto> opt = productoRepository.findByNombre(nombre);
        if (opt.isPresent()) {
            Producto p = opt.get();
            p.setStock(stock);
            productoRepository.save(p);
            return true;
        }
        return false;
    }

    // Elimina un producto (Se mantiene igual)
    public boolean eliminarProducto(String nombre) {
        Optional<Producto> opt = productoRepository.findByNombre(nombre);
        if (opt.isPresent()) {
            productoRepository.delete(opt.get());
            return true;
        }
        return false;
    }
}