package com.ChillCoffeV1_4.controlador;



import com.ChillCoffeV1_4.modelo.Usuario;

import com.ChillCoffeV1_4.servicio.Clien_comprasService;

import com.ChillCoffeV1_4.servicio.UsuarioService; // Asegúrate de que este servicio exista

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller

@RequestMapping("/Clien_ventas") // Agregamos un RequestMapping base para rutas limpias

public class Clien_ventasController {



    private final Clien_comprasService compraService;

    private final UsuarioService usuarioService;



    // Inyección de Dependencias

    public Clien_ventasController(Clien_comprasService compraService, UsuarioService usuarioService) {

        this.compraService = compraService;

        this.usuarioService = usuarioService;

    }



    // Helper method para autenticación básica

    private boolean isAuthenticated(HttpSession session) {

         return session.getAttribute("nombre") != null;

    }



    // Mapeo principal para mostrar la página de ventas/compras

    @GetMapping

    public String verVentas(HttpSession session, Model model) {

        if (!isAuthenticated(session)) {

            return "redirect:/login";

        }



        // Cargar productos disponibles y carrito

        model.addAttribute("productos", compraService.obtenerProductosDisponibles());

        // CLAVE CORREGIDA: Usa 'carritoItems' para coincidir con tu HTML

        model.addAttribute("carritoItems", compraService.obtenerCarrito(session));



        // Cargar usuario

        String nombreUsuario = (String) session.getAttribute("nombre");

        if (nombreUsuario != null) {

            Usuario usuario = usuarioService.buscarPorNombre(nombreUsuario);

            if (usuario != null) {

                model.addAttribute("usuario", usuario);

            }

        }

       

        return "Clien_ventas";

    }



    // Endpoint para agregar productos al carrito: /Clien_ventas/agregar

    @PostMapping("/agregar")

    public String agregarProducto(@RequestParam("productoId") Long productoId, @RequestParam(value = "cantidad", defaultValue = "1") int cantidad, HttpSession session, RedirectAttributes redirectAttributes) {

       

        boolean success = compraService.agregarProductoACarrito(session, productoId, cantidad);

       

        if (!success) {

             redirectAttributes.addFlashAttribute("error", "No hay stock suficiente o el producto no existe.");

        }

       

        return "redirect:/Clien_ventas";

    }

   

    // Endpoint para eliminar un producto del carrito

    @PostMapping("/eliminar") // Si tu HTML tiene una ruta de eliminación, ajusta aquí.

    public String eliminarProducto(@RequestParam("productoId") Long productoId, HttpSession session) {

        compraService.eliminarProductoDeCarrito(session, productoId);

        return "redirect:/Clien_ventas";

    }



    // Endpoint para limpiar el carrito: /Clien_ventas/limpiar

    @GetMapping("/limpiar")

    public String limpiarCarrito(HttpSession session, RedirectAttributes redirectAttributes) {

        compraService.limpiarCarrito(session);

        redirectAttributes.addFlashAttribute("mensaje", "El carrito ha sido vaciado.");

        return "redirect:/Clien_ventas";

    }



    // Endpoint para obtener el total del carrito vía AJAX: /Clien_ventas/total

    @GetMapping("/total")

    @ResponseBody

    public ResponseEntity<Double> getTotalCarrito(HttpSession session) {

        Double total = compraService.calcularTotal(session);

        return ResponseEntity.ok(total);

    }



    // Procesa la compra (checkout): /Clien_ventas/checkout

    @PostMapping("/checkout")

    public String procesarCheckout(HttpSession session, RedirectAttributes redirectAttributes) {

       

        boolean success = compraService.procesarCheckout(session);



        if (success) {

            redirectAttributes.addFlashAttribute("mensaje", "¡Compra realizada con éxito! Stock actualizado.");

        } else {

            redirectAttributes.addFlashAttribute("error", "Error: Stock insuficiente para completar la compra o el carrito estaba vacío.");

        }



        return "redirect:/Clien_ventas";

    }

}