package com.ChillCoffeV1_4.servicio;

import com.ChillCoffeV1_4.modelo.Cupon;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CuponService {

    private final List<Cupon> cupones = new ArrayList<>();

    public boolean crearOActualizarCupon(String codigo, int descuento) {
        for (Cupon c : cupones) {
            if (c.getCodigo().equalsIgnoreCase(codigo)) {
                c.setDescuento(descuento);
                return true; // actualizado
            }
        }
        cupones.add(new Cupon(codigo, descuento));
        return true; // creado
    }

    public List<Cupon> obtenerCupones() {
        return cupones;
    }

    public boolean eliminarCupon(String codigo) {
        return cupones.removeIf(c -> c.getCodigo().equalsIgnoreCase(codigo));
    }
}
