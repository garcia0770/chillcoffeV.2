package com.ChillCoffeV1_4.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordenes_historial")
public class OrdenHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ordenOriginalId;
    private String nombre;
    private LocalDateTime creadoEn;
    private LocalDateTime procesadoEn;
    private String accion; // "CREADA", "ATENDIDA", "ELIMINADA"

    public OrdenHistorial() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrdenOriginalId() { return ordenOriginalId; }
    public void setOrdenOriginalId(Long ordenOriginalId) { this.ordenOriginalId = ordenOriginalId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    public LocalDateTime getProcesadoEn() { return procesadoEn; }
    public void setProcesadoEn(LocalDateTime procesadoEn) { this.procesadoEn = procesadoEn; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
}