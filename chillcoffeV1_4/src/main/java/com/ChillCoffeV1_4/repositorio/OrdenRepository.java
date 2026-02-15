package com.ChillCoffeV1_4.repositorio;

import com.ChillCoffeV1_4.modelo.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
List<Orden> findByActivoTrueOrderByCreadoEnDesc();
}