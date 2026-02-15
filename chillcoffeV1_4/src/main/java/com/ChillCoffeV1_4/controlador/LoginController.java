package com.ChillCoffeV1_4.controlador;

import com.ChillCoffeV1_4.modelo.Rol;
import com.ChillCoffeV1_4.modelo.Usuario;
import com.ChillCoffeV1_4.servicio.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final UsuarioService usuarioService;
    private final BCryptPasswordEncoder passwordEncoder;

    public LoginController(UsuarioService usuarioService, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. Muestra la página de login y verifica la sesión
    @GetMapping({"/", "/login"})
    public String loginPage(HttpSession session) { // Agregamos HttpSession
        // ⭐ CORRECCIÓN/MEJORA: Redirigir si la sesión ya existe
        if (session.getAttribute("rol") != null) {
            String rol = (String) session.getAttribute("rol");
            if ("admin".equalsIgnoreCase(rol)) {
                return "redirect:/admin";
            } else if ("cliente".equalsIgnoreCase(rol) || "invitado".equalsIgnoreCase(rol)) {
                return "redirect:/user_panel";
            }
        }
        return "Login";
    }

    // 2. Procesa la solicitud POST de login
    @PostMapping("/login")
    public String doLogin(
            @RequestParam String username, 
            @RequestParam String password, 
            Model model,
            HttpSession session) {

        // Busca al usuario por CORREO
        Usuario u = usuarioService.buscarPorCorreo(username);

        // Verifica si el usuario existe y si la clave encriptada coincide
        if (u != null && passwordEncoder.matches(password, u.getPassword())) {

            // ⭐ CORRECCIÓN CLAVE: Guarda el ROL como String (u.getRol().name())
            session.setAttribute("nombre", u.getNombre());
            session.setAttribute("rol", u.getRol().name()); // Rol.admin -> "ADMIN"

            switch (u.getRol()) {
                case admin:
                    return "redirect:/admin"; // Redirigirá a AdminController
                case cliente:
                case invitado:
                    return "redirect:/user_panel";
                default:
                    session.invalidate(); // Si el rol es desconocido, invalida la sesión
                    return "redirect:/login";
            }
        }

        model.addAttribute("error", "Usuario o contraseña incorrectos");
        return "Login";
    }

    // 3. Muestra el panel del usuario
    @GetMapping("/user_panel")
    public String userPanel(HttpSession session, Model model) {

        Object rol = session.getAttribute("rol");
        
        // ⭐ CORRECCIÓN/MEJORA: Si no tiene sesión, lo enviamos al login
        if (rol == null) {
            return "redirect:/login";
        }

        model.addAttribute("nombre", session.getAttribute("nombre"));

        return "User_panel"; // Asumo que el nombre de la plantilla es "User_panel.html"
    }

    // 4. Cierre de sesión
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // 5. Login de invitado
    @GetMapping("/invitado")
    public String invitadoPage(HttpSession session) {
        // ⭐ Asegúrate de que tu enum Rol tenga 'invitado'
        session.setAttribute("rol", Rol.invitado.name());
        session.setAttribute("nombre", "Invitado");

        return "redirect:/user_panel";
    }
}