package com.ChillCoffeV1_4.modelo;

public class Descuento {
    private String producto;
    private int porcentaje;
    private boolean activo;

    public Descuento() {}

    public Descuento(String producto, int porcentaje) {
        this.producto = producto.trim().toLowerCase();
        this.porcentaje = porcentaje;
        this.activo = true;
    }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto.trim().toLowerCase(); }

    public int getPorcentaje() { return porcentaje; }
    public void setPorcentaje(int porcentaje) { this.porcentaje = porcentaje; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}

