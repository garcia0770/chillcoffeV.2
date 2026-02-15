package com.ChillCoffeV1_4.repositorio;

import com.ChillCoffeV1_4.modelo.OrdenHistorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenHistorialRepository extends JpaRepository<OrdenHistorial, Long> {

}