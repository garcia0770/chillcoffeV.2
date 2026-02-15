package com.ChillCoffeV1_4.controlador;

import com.ChillCoffeV1_4.servicio.Admin_inventarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/inventario")
public class Admin_InventarioController { 

    @Autowired 
    private Admin_inventarioService inventarioService;

    private boolean esAdmin(HttpSession session) {
        Object rol = session.getAttribute("rol");
        return rol != null && "admin".equals(rol.toString());
    }

    // 1. Muestra la vista de inventario (GET /admin/inventario)
    @GetMapping("")
    public String verInventario(HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/Login";

        model.addAttribute("productos", inventarioService.obtenerProductos());
        model.addAttribute("nombre", session.getAttribute("nombre"));

        if (model.asMap().containsKey("mensaje")) {
            model.addAttribute("mensaje", model.asMap().get("mensaje"));
        }

        return "Admin_inventario"; 
    }

    // 2. Guarda o modifica un producto (POST /admin/inventario/guardar)
    @PostMapping("/guardar")
    public String guardar(@RequestParam String nombre, 
                          @RequestParam(required = false) Integer stock, 
                          // ⭐ CORRECCIÓN: Usar Double para el precio ⭐
                          @RequestParam(required = false) Double precio, 
                          RedirectAttributes redirectAttrs) {

        if (stock == null) stock = 0;
        // ⭐ CORRECCIÓN: Usar 0.0 (Double) si es null ⭐
        if (precio == null) precio = 0.0; 
        
        inventarioService.guardarProducto(nombre.trim(), stock, precio);
        redirectAttrs.addFlashAttribute("mensaje", "Producto " + nombre + " guardado/actualizado con éxito.");
        return "redirect:/admin/inventario";
    }

    // 3. Maneja la actualización de stock (POST /admin/inventario/actualizarstock)
    @PostMapping("/actualizarstock")
    public String actualizarStock(@RequestParam String nombre,
                                     @RequestParam Integer stock,
                                     RedirectAttributes redirectAttrs) {
        
        if (inventarioService.actualizarSoloStock(nombre.trim(), stock)) {
            redirectAttrs.addFlashAttribute("mensaje", "Stock de " + nombre + " actualizado.");
        } else {
            redirectAttrs.addFlashAttribute("mensaje", "Error al actualizar stock o producto no encontrado.");
        }
        return "redirect:/admin/inventario";
    }

    // 4. Maneja la acción de eliminar (POST /admin/inventario/eliminar)
    @PostMapping("/eliminar")
    public String eliminar(@RequestParam String nombre, RedirectAttributes redirectAttrs) {
        if (inventarioService.eliminarProducto(nombre.trim())) {
            redirectAttrs.addFlashAttribute("mensaje", "Producto " + nombre + " eliminado con éxito.");
        } else {
            redirectAttrs.addFlashAttribute("mensaje", "Error: Producto no encontrado para eliminar.");
        }
        return "redirect:/admin/inventario";
    }
}