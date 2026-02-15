package com.ChillCoffeV1_4.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // ⭐ AÑADE ESTA LÍNEA ⭐
    @Column(name = "id_orden") // <-- ¡REEMPLAZA "id_orden" con el nombre real de la columna ID en tu tabla 'orders'!
    private Long id;

    private String nombre;

    private LocalDateTime creadoEn = LocalDateTime.now();

    private boolean activo = true;

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}