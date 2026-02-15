package com.ChillCoffeV1_4.controlador;

import com.ChillCoffeV1_4.servicio.DescuentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/descuentos")
public class DescuentoController {

    @Autowired
    private DescuentoService descuentoService;

    @GetMapping
    public String verDescuentos(Model model) {
        model.addAttribute("descuentos", descuentoService.obtenerDescuentos());
        return "admin_descuentos";
    }

    @PostMapping("/crear")
    public String crearDescuento(@RequestParam String producto,
                                    @RequestParam int porcentaje,
                                    Model model) {
        descuentoService.crearDescuento(producto, porcentaje);
        model.addAttribute("mensaje", "Descuento creado");
        model.addAttribute("descuentos", descuentoService.obtenerDescuentos());
        return "admin_descuentos";
    }

    @PostMapping("/eliminar")
    public String eliminarDescuento(@RequestParam String producto, Model model) {
        descuentoService.eliminarDescuento(producto);
        model.addAttribute("mensaje", "Descuento eliminado");
        model.addAttribute("descuentos", descuentoService.obtenerDescuentos());
        return "admin_descuentos";
    }
}
