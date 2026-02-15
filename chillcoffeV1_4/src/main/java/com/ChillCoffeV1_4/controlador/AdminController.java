package com.ChillCoffeV1_4.controlador;

import com.ChillCoffeV1_4.modelo.CafeteriaItem;
import com.ChillCoffeV1_4.modelo.Venta; 
import com.ChillCoffeV1_4.servicio.StackQueueService;
import com.ChillCoffeV1_4.servicio.Admin_ventaService; 
import com.ChillCoffeV1_4.servicio.Admin_inventarioService; 

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List; 
import java.util.Optional; 
import java.util.Objects; 

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final StackQueueService sqService;
    private final Admin_inventarioService inventarioService;
    private final Admin_ventaService ventaService; 


    // Constructor con inyección de dependencias
    public AdminController(StackQueueService sqService, 
                            Admin_inventarioService inventarioService, 
                            Admin_ventaService ventaService) {
        this.sqService = sqService;
        this.inventarioService = inventarioService;
        this.ventaService = ventaService;
    }

    // Método auxiliar para chequear admin
    private boolean esAdmin(HttpSession session) {
        Object rol = session.getAttribute("rol");
        return rol != null && "admin".equals(rol.toString());
    }

    // --- 1. Home del Admin Panel ---
    @GetMapping
    public String adminHome(HttpSession session, Model model) {
        
        if (!esAdmin(session)) {
            return "redirect:/Login";
        }

        model.addAttribute("nombre", session.getAttribute("nombre"));
        model.addAttribute("pila", sqService.obtenerPila());
        model.addAttribute("cola", sqService.obtenerCola());

        // Recupera el mensaje flash si existe (para mostrar después de pop/dequeue)
        String msg = (String) model.asMap().get("msg"); 
        if (msg != null) model.addAttribute("mensaje", msg);

        return "Admin_panel";
    }

    // --- 2. Historial de Ventas ---
    @GetMapping("/ventas")
    public String verHistorialVentas(HttpSession session, Model model, 
                                     @RequestParam(value = "search", required = false) String search) {
        
        if (!esAdmin(session)) return "redirect:/Login";

        List<Venta> ventas;
        if (search != null && !search.trim().isEmpty()) {
            ventas = ventaService.buscarVentas(search);
            model.addAttribute("terminoBusqueda", search); 
        } else {
            ventas = ventaService.obtenerTodas();
        }

        model.addAttribute("ventas", ventas); 
        model.addAttribute("nombre", session.getAttribute("nombre"));
        
        // Recupera el mensaje flash si existe (para mostrar después de eliminar)
        String msg = (String) model.asMap().get("msg");
        if (msg != null) model.addAttribute("msg", msg);
        
        return "Admin_ventas";
    }

    // --- 3. Eliminar Venta ---
    @PostMapping("/ventas/eliminar/{id}")
    public String eliminarVenta(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        try {
            ventaService.eliminarVentaPorId(id);
            redirectAttrs.addFlashAttribute("msg", "Registro de venta ID " + id + " eliminado correctamente.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("msg", "Error al eliminar la venta ID " + id + ": " + e.getMessage());
        }
        return "redirect:/admin/ventas";
    }


    // --- 4. Órdenes urgentes (PILA) ---
    @PostMapping("/pila/push")
    public String pushPila(@RequestParam String nombre,
                            @RequestParam(defaultValue = "1") int cantidad,
                            RedirectAttributes redirectAttrs) {
        CafeteriaItem item = new CafeteriaItem(nombre.trim(), cantidad);
        sqService.pushPila(item);
        // ⭐ CORRECCIÓN: Usar addFlashAttribute para mensajes después de redirección
        redirectAttrs.addFlashAttribute("msg", "Orden urgente agregada: " + nombre + " x" + cantidad);
        return "redirect:/admin";
    }

    @PostMapping("/pila/pop")
    public String popPila(HttpSession session, RedirectAttributes redirectAttrs) {
        Optional<CafeteriaItem> opt = sqService.popPila(); 
        
        if (opt.isPresent()) {
            CafeteriaItem item = opt.get();
            
            boolean ok = inventarioService.descontarStockPorNombre(item.getNombre(), item.getCantidad()); 

            if (ok) {
                // Se asume que obtenerPrecioPorNombre devuelve Double
                Double precioReal = inventarioService.obtenerPrecioPorNombre(item.getNombre());
                
                String vendedor = Objects.toString(session.getAttribute("nombre"), "Usuario Desconocido");
                
                // Venta acepta Double para precioReal
                Venta nuevaVenta = new Venta(item.getNombre(), item.getCantidad(), precioReal, vendedor, "Admin");
                ventaService.guardarVenta(nuevaVenta);

                // ⭐ CORRECCIÓN: Usar addFlashAttribute para mensajes después de redirección
                redirectAttrs.addFlashAttribute("msg", "Venta urgente registrada y guardada: " + item.getNombre());
            } else {
                // ⭐ CORRECCIÓN: Usar addFlashAttribute para mensajes después de redirección
                redirectAttrs.addFlashAttribute("msg", "Stock insuficiente para " + item.getNombre() + ". Se descontó de la lista, pero no se registró la venta.");
            }
        } else {
            // ⭐ CORRECCIÓN: Usar addFlashAttribute para mensajes después de redirección
            redirectAttrs.addFlashAttribute("msg", "No hay órdenes urgentes");
        }
        return "redirect:/admin";
    }

    @PostMapping("/pila/clear")
    public String clearPila(RedirectAttributes redirectAttrs) {
        sqService.limpiarPila();
        // ⭐ CORRECCIÓN: Usar addFlashAttribute para mensajes después de redirección
        redirectAttrs.addFlashAttribute("msg", "Órdenes urgentes limpiadas");
        return "redirect:/admin";
    }

    // --- 5. Pedidos normales (COLA) ---
    @PostMapping("/cola/enqueue")
    public String enqueueCola(@RequestParam String nombre,
                            @RequestParam(defaultValue = "1") int cantidad,
                            RedirectAttributes redirectAttrs) {
        CafeteriaItem item = new CafeteriaItem(nombre.trim(), cantidad);
        sqService.enqueueCola(item);
        // ⭐ CORRECCIÓN: Usar addFlashAttribute para mensajes después de redirección
        redirectAttrs.addFlashAttribute("msg", "Pedido agregado: " + nombre + " x" + cantidad);
        return "redirect:/admin";
    }

    @PostMapping("/cola/dequeue")
    public String dequeueCola(HttpSession session, RedirectAttributes redirectAttrs) {
        Optional<CafeteriaItem> opt = sqService.dequeueCola(); 
        
        if (opt.isPresent()) {
            CafeteriaItem item = opt.get();
            
            boolean ok = inventarioService.descontarStockPorNombre(item.getNombre(), item.getCantidad()); 

            if (ok) {
                // Se asume que obtenerPrecioPorNombre devuelve Double
                Double precioReal = inventarioService.obtenerPrecioPorNombre(item.getNombre());
                
                String vendedor = Objects.toString(session.getAttribute("nombre"), "Usuario Desconocido");
                
                // Venta acepta Double para precioReal
                Venta nuevaVenta = new Venta(item.getNombre(), item.getCantidad(), precioReal, vendedor, "Admin");
                ventaService.guardarVenta(nuevaVenta);
                
                // ⭐ CORRECCIÓN: Usar addFlashAttribute para mensajes después de redirección
                redirectAttrs.addFlashAttribute("msg", "Venta registrada y guardada: " + item.getNombre());
            } else {
                // ⭐ CORRECCIÓN: Usar addFlashAttribute para mensajes después de redirección
                redirectAttrs.addFlashAttribute("msg", "Stock insuficiente para " + item.getNombre() + ". Se descontó de la lista, pero no se registró la venta.");
            }
        } else {
            // ⭐ CORRECCIÓN: Usar addFlashAttribute para mensajes después de redirección
            redirectAttrs.addFlashAttribute("msg", "No hay pedidos pendientes");
        }
        return "redirect:/admin";
    }

    @PostMapping("/cola/clear")
    public String clearCola(RedirectAttributes redirectAttrs) {
        sqService.limpiarCola();
        // ⭐ CORRECCIÓN: Usar addFlashAttribute para mensajes después de redirección
        redirectAttrs.addFlashAttribute("msg", "Pedidos pendientes limpiados");
        return "redirect:/admin";
    }

    // --- 6. Manejo de Cupones ---
    @GetMapping("/cupones")
    public String cuponesString(HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/login";
        model.addAttribute("nombre", session.getAttribute("nombre"));
        return "Admin_cupones";
    }
}