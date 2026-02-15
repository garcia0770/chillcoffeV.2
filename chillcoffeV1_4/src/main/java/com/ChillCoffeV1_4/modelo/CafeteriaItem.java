package com.ChillCoffeV1_4.modelo;

import java.time.LocalDateTime;

public class CafeteriaItem {
    private static int contador = 1;

    private int id;
    private String nombre;
    private int cantidad;
    private LocalDateTime creadoEn;

    public CafeteriaItem() {
        this.id = contador++;
        this.creadoEn = LocalDateTime.now();
    }

    public CafeteriaItem(String nombre, int cantidad) {
        this.id = contador++;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.creadoEn = LocalDateTime.now();
    }

    public int getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}
