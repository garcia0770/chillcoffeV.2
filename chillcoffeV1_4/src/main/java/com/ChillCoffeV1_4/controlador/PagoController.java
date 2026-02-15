package com.ChillCoffeV1_4.controlador;

import com.ChillCoffeV1_4.modelo.Venta;
import com.ChillCoffeV1_4.servicio.VentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PagoController {

    private final VentaService ventaService;

    // Constructor para inyección de dependencia del servicio
    public PagoController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    // 1. Método para mostrar la página de pagos
    // Debes cambiar la URL a donde envíes al cliente después de armar el pedido
    @GetMapping("/metodos_pago")
    public String mostrarPaginaPago(Model model, HttpSession session) {
        // ⭐ EJEMPLO: Obtener el Total y la Orden de la Sesión
        // Debes implementar esta lógica de cómo se guarda la orden en la sesión.
        Double totalPagar = (Double) session.getAttribute("total_pedido");
        Venta pedidoActual = (Venta) session.getAttribute("pedido_actual");
        
        if (totalPagar == null || pedidoActual == null) {
             // Si no hay pedido en sesión, redirigir al panel de cliente
            return "redirect:/user_panel"; 
        }

        model.addAttribute("totalPagar", totalPagar);
        // Aquí podrías agregar los detalles del recibo
        
        return "metodos_pago";
    }

    // 2. Método para procesar el pago final
    @PostMapping("/procesar_pago")
    public String procesarPago(
            // ⭐ IMPORTANTE: Asumo que en el formulario ocultarás un campo para el método de pago
            @RequestParam(name = "metodoPago", required = false) String metodoPagoSeleccionado,
            HttpSession session,
            Model model) {

        // Obtener el ID del usuario logueado de la sesión
        Integer idUsuario = (Integer) session.getAttribute("id_usuario");
        Venta pedidoActual = (Venta) session.getAttribute("pedido_actual");

        if (idUsuario == null || pedidoActual == null) {
            model.addAttribute("error", "Error: Sesión o pedido no encontrado.");
            return "redirect:/login"; 
        }

        try {
            // Llama al servicio para registrar la venta y el método de pago
            Venta ventaFinalizada = ventaService.finalizarVenta(pedidoActual, metodoPagoSeleccionado);

            // Limpiar la sesión después de la compra
            session.removeAttribute("pedido_actual");
            session.removeAttribute("total_pedido");

            // Redirigir a una página de confirmación
            return "redirect:/confirmacion_pago?id=" + ventaFinalizada.getId(); 

        } catch (Exception e) {
            // Manejo de errores (ej. fallo al guardar en DB)
            model.addAttribute("error", "Error al procesar el pago: " + e.getMessage());
            return "metodos_pago";
        }
    }
}