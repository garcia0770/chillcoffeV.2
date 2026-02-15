// Archivo: ProductoRepository.java
package com.ChillCoffeV1_4.repositorio;

import com.ChillCoffeV1_4.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Método requerido por InventarioService para buscar si existe un producto por nombre
    Optional<Producto> findByNombre(String nombre); 
}