package com.ChillCoffeV1_4.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.ChillCoffeV1_4.modelo.Usuario;
import com.ChillCoffeV1_4.servicio.UsuarioService;

@Controller
public class Recuperar_contrasenaController {

    private final UsuarioService usuarioService;

    public Recuperar_contrasenaController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // FORM: ingresar correo
    @GetMapping("/recuperar")
    public String recuperarPage() {
        return "recuperar";
    }

    @PostMapping("/recuperar")
    public String procesarCorreo(@RequestParam String correo, Model model) {

        if (!usuarioService.correoExiste(correo)) {
            model.addAttribute("error", "Este correo no está registrado.");
            return "recuperar";
        }

        // Generar token
        String token = usuarioService.generarTokenRecuperacion(correo);

        System.out.println("TOKEN GENERADO => " + token);

        usuarioService.enviarCorreoRecuperacion(correo, token);

        String link = "http://localhost:8080/cambiar?token=" + token;
        model.addAttribute("debugLink", link);

        model.addAttribute("mensaje", "Se envió un enlace a tu correo.");
        model.addAttribute("volverLogin", true);

        return "recuperar";
    }

    // FORM: cambiar contraseña usando token
    @GetMapping("/cambiar")
    public String cambiarPage(@RequestParam String token, Model model) {

        if (!usuarioService.tokenValido(token)) {
            model.addAttribute("error", "El enlace no es válido o expiró.");
            return "recuperar";
        }

        model.addAttribute("token", token);
        return "cambiar_contrasena";
    }

    @PostMapping("/cambiar")
    public String cambiarContrasena(
            @RequestParam String token,
            @RequestParam String password1,
            @RequestParam String password2,
            Model model) {

        // Validar token
        if (!usuarioService.tokenValido(token)) {
            model.addAttribute("error", "El enlace expiró o no es válido.");
            return "recuperar";
        }

        // Validar contraseñas iguales
        if (!password1.equals(password2)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            model.addAttribute("token", token);
            return "cambiar_contrasena";
        }

        // Obtener usuario a partir del token
        Usuario usuario = usuarioService.getUsuarioPorToken(token);

        if (usuario == null) {
            model.addAttribute("error", "Hubo un error con el usuario.");
            return "recuperar";
        }

        // Actualizar contraseña
        usuarioService.actualizarContrasena(usuario, password1);

        // 🔥 Redirigir al login con mensaje
        return "redirect:/login?passwordChanged";
    }
}
