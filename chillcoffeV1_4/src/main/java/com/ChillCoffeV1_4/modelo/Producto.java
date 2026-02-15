package com.ChillCoffeV1_4.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    private String nombre;
    private Integer stock; 
    
    // ⭐ CAMBIO CRÍTICO: Double para dinero (alineado a DECIMAL en SQL) ⭐
    private Double precio; 

    // Constructor vacío (necesario para JPA)
    public Producto() {}

    // Getters y Setters existentes
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    // ⭐ GETTERS Y SETTERS DE PRECIO ACTUALIZADOS ⭐
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    // ⭐ FIN GETTERS Y SETTERS DE PRECIO ACTUALIZADOS ⭐
    
    // El método getPrecioDouble() ya no es necesario si el getter principal devuelve Double,
    // pero lo mantenemos por si el Thymeleaf lo requiere.
    public Double getPrecioDouble() {
        return this.precio != null ? this.precio : 0.0;
    }
}