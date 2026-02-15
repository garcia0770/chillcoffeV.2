package com.ChillCoffeV1_4.modelo;



import jakarta.persistence.*;

import java.time.LocalDateTime;

import java.util.Date;



@Entity

@Table(name = "ventas")

public class Venta {



    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;



    private String producto;

    private int cantidad;

   

    // ⭐ CAMBIO CRÍTICO: Double para dinero (alineado a DECIMAL en SQL) ⭐

    @Column(name = "precio_unitario")

    private Double precioUnitario;

   

    private Double total;

    // ⭐ FIN CAMBIO CRÍTICO ⭐

   

    private String usuario;

    private String rol;

    private LocalDateTime fecha;



    public Venta() {

        this.fecha = LocalDateTime.now();

    }



    // ⭐ CONSTRUCTOR ACTUALIZADO CON DOUBLE ⭐

    public Venta(String producto, int cantidad, Double precioUnitario, String usuario, String rol) {

        this.producto = producto;

        this.cantidad = cantidad;

        this.precioUnitario = precioUnitario;

        this.total = cantidad * precioUnitario; // Cálculo usando Double

        this.usuario = usuario;

        this.rol = rol;

        this.fecha = LocalDateTime.now();

    }



    // Getters y Setters

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getProducto() { return producto; }

    public void setProducto(String producto) { this.producto = producto; }

    public int getCantidad() { return cantidad; }

    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

   

    // ⭐ GETTERS Y SETTERS DE DINERO ACTUALIZADOS ⭐

    public Double getPrecioUnitario() { return precioUnitario; }

    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }

   

    public Double getTotal() { return total; }

    public void setTotal(Double total) { this.total = total; }

    // ⭐ FIN GETTERS Y SETTERS DE DINERO ACTUALIZADOS ⭐

   

    public String getUsuario() { return usuario; }

    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getRol() { return rol; }

    public void setRol(String rol) { this.rol = rol; }

    public LocalDateTime getFecha() { return fecha; }

    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }



    public void setMetodoPago(String metodoPago) {

        // TODO Auto-generated method stub

        throw new UnsupportedOperationException("Unimplemented method 'setMetodoPago'");

    }



    public void setFecha(Date fecha2) {

        // TODO Auto-generated method stub

        throw new UnsupportedOperationException("Unimplemented method 'setFecha'");

    }

}