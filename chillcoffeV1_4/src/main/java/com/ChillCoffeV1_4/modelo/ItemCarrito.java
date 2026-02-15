package com.ChillCoffeV1_4.modelo;

public class ItemCarrito {
    private Long id; // ID del producto
    private String nombre;
    // 🛑 CORRECCIÓN: Cambiado de Integer a Double para consistencia con los cálculos y el HTML
    private Double precio; 
    private int cantidad;

    // ✅ CORRECCIÓN CLAVE: El constructor ahora espera Double para el precio.
    public ItemCarrito(Long id, String nombre, Double precio, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }
    
    // Constructor vacío (recomendado para Spring/Hibernate)
    public ItemCarrito() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // 🛑 CORRECCIÓN: El Getter ahora devuelve Double
    public Double getPrecio() {
        return precio;
    }

    // 🛑 CORRECCIÓN: El Setter ahora acepta Double
    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}