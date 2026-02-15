package com.ChillCoffeV1_4.repositorio;

import com.ChillCoffeV1_4.modelo.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    
    /**
     * Busca ventas cuyo nombre de producto contenga el término dado, ignorando mayúsculas/minúsculas.
     * @param producto el término a buscar
     * @return Lista de objetos Venta que coinciden con el término.
     */
    List<Venta> findByProductoContainingIgnoreCase(String producto);
    
    // Spring Data JPA ya proporciona deleteById(Long id) para eliminar.
}