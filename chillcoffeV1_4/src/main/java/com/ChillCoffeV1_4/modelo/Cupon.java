package com.ChillCoffeV1_4.modelo;

import java.time.LocalDateTime;

public class Cupon {
    private String codigo;
    private int descuento;
    private LocalDateTime fechaCreacion;
    private boolean activo;

    public Cupon(String codigo, int descuento) {
        this.codigo = codigo;
        this.descuento = descuento;
        this.fechaCreacion = LocalDateTime.now();
        this.activo = true;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public int getDescuento() { return descuento; }
    public void setDescuento(int descuento) { this.descuento = descuento; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
