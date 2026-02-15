package com.ChillCoffeV1_4.controlador;

import com.ChillCoffeV1_4.modelo.Usuario;
import com.ChillCoffeV1_4.servicio.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistroController {

    private final UsuarioService usuarioService;

    public RegistroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(
            @ModelAttribute Usuario usuario,
            @RequestParam String password2,
            Model model) {

        if (!usuario.getPassword().equals(password2)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "registro";
        }

        boolean registrado = usuarioService.registrarUsuario(usuario);

        if (!registrado) {
            model.addAttribute("error", "El correo ya está registrado");
            return "registro";
        }

        model.addAttribute("mensaje", "Cuenta creada con éxito");
        return "login";
    }
}