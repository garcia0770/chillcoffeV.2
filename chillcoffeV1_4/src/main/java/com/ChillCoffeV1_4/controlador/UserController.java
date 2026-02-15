package com.ChillCoffeV1_4.controlador;

import com.ChillCoffeV1_4.modelo.Usuario;
import com.ChillCoffeV1_4.servicio.UsuarioService; // 💡 Necesitas importar el servicio
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // 💡 Necesitas importar Model
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping 
public class UserController {

    private final UsuarioService usuarioService; 

    // Constructor para inyectar el servicio de Usuario
    public UserController(UsuarioService usuarioService) { 
        this.usuarioService = usuarioService;
    }

    /**
     * Helper method to check if the user is authenticated (has a 'rol' in session).
     */
    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("rol") != null;
    }
    
    // =========================================================================
    // Mapeo para /perfil (Muestra la vista) ⭐ ESTE ES EL CAMBIO CLAVE ⭐
    // =========================================================================
    @GetMapping("/perfil")
    public String miPerfil(HttpSession session, Model model) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        String nombreUsuario = (String) session.getAttribute("nombre");
        
        // --- 1. Caso Invitado / No Logueado (Aunque esto debería ser evitado por isAuthenticated) ---
        if (nombreUsuario == null) {
             // Si el usuario está autenticado por rol, pero no tiene nombre, redirigimos a login
             return "redirect:/login"; 
        }

        // --- 2. Caso Usuario Registrado ---
        Usuario usuario = usuarioService.buscarPorNombre(nombreUsuario);

        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("esInvitado", false);
            return "Mi_perfil"; // Retorna la vista Mi_perfil.html
        } else {
            session.invalidate(); 
            return "redirect:/login"; 
        }
    }
    
    // =========================================================================
    // Mapeo para /pagos
    // Muestra la plantilla 'metodos_pago.html'.
    // =========================================================================
    @GetMapping("/pagos")
    public String metodosPago(HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        return "metodos_pago"; 
    }
}