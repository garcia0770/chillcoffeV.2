package com.ChillCoffeV1_4.controlador;

import com.ChillCoffeV1_4.servicio.CuponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cupones")
public class CuponController {

    @Autowired
    private CuponService cuponService;

    @GetMapping
    public String mostrarCupones(Model model) {
        model.addAttribute("cupones", cuponService.obtenerCupones());
        return "admin_cupones";
    }

    @PostMapping("/crear")
    public String crearOActualizarCupon(@RequestParam String codigo, @RequestParam int descuento, Model model) {
        cuponService.crearOActualizarCupon(codigo, descuento);
        model.addAttribute("mensaje", "Cupón creado o actualizado");
        model.addAttribute("cupones", cuponService.obtenerCupones());
        return "admin_cupones";
    }

    @PostMapping("/eliminar")
    public String eliminarCupon(@RequestParam String codigo, Model model) {
        boolean eliminado = cuponService.eliminarCupon(codigo);
        model.addAttribute("mensaje", eliminado ? "Cupón eliminado" : "Cupón no encontrado");
        model.addAttribute("cupones", cuponService.obtenerCupones());
        return "admin_cupones";
    }
}
