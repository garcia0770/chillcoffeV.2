package com.ChillCoffeV1_4.servicio;

import com.ChillCoffeV1_4.modelo.Descuento;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DescuentoService {

    private final List<Descuento> descuentos = new ArrayList<>();

    public void crearDescuento(String producto, int porcentaje) {
        descuentos.add(new Descuento(producto, porcentaje));
    }

    public int obtenerDescuento(String producto) {
        return descuentos.stream()
                .filter(d -> d.isActivo() && d.getProducto().equalsIgnoreCase(producto))
                .map(Descuento::getPorcentaje)
                .findFirst()
                .orElse(0);
    }

    public List<Descuento> obtenerDescuentos() {
        return descuentos;
    }

    public boolean eliminarDescuento(String producto) {
        return descuentos.removeIf(d -> d.getProducto().equalsIgnoreCase(producto));
    }
}
