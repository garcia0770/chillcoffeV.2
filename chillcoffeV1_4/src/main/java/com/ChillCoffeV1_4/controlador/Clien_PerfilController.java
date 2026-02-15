package com.ChillCoffeV1_4.controlador;

import com.ChillCoffeV1_4.modelo.Usuario;
import com.ChillCoffeV1_4.servicio.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class Clien_PerfilController {

    private final UsuarioService usuarioService; 

    public Clien_PerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // =========================================================================
    // Mapeo para POST /perfil/guardar (Controla la lógica de actualización)
    // =========================================================================
    
    /**
     * Procesa la solicitud POST para guardar los cambios del perfil, preservando password y rol.
     */
    @PostMapping("/perfil/guardar") // ⭐ Mapeo completo aquí ⭐
    public String guardarPerfil(@ModelAttribute Usuario usuarioForm, 
                                 HttpSession session, 
                                 RedirectAttributes redirectAttributes) {
        
        String nombreUsuario = (String) session.getAttribute("nombre");

        // 1. Verificación básica de sesión
        if (nombreUsuario == null) {
            redirectAttributes.addFlashAttribute("error", "Sesión expirada. Por favor, inicia sesión.");
            return "redirect:/perfil";
        }

        // 2. Obtener el usuario Original (buscándolo por nombre, ya que está en la sesión)
        Usuario usuarioOriginal = usuarioService.buscarPorNombre(nombreUsuario); 

        // 3. Validación de integridad y existencia
        if (usuarioOriginal == null || !usuarioForm.getId().equals(usuarioOriginal.getId())) { 
             // ⭐ CORRECCIÓN CLAVE: Se cambió getIdUsuario() por getId()
             redirectAttributes.addFlashAttribute("error", "Error de seguridad o usuario no encontrado.");
             return "redirect:/perfil";
        }

        try {
            // 4. TRANSFERENCIA DE CAMPOS SENSIBLES (Seguridad Clave)
            // Aseguramos que la contraseña, rol y token no se pierdan o cambien
            usuarioForm.setPassword(usuarioOriginal.getPassword()); 
            usuarioForm.setRol(usuarioOriginal.getRol());          
            usuarioForm.setResetToken(usuarioOriginal.getResetToken());
            
            // 6. Guardar en la base de datos (solo actualiza campos modificables)
            usuarioService.actualizarPerfil(usuarioForm); 

            // 7. Si se cambió el nombre, actualiza la sesión
            if (!usuarioForm.getNombre().equals(nombreUsuario)) {
                session.setAttribute("nombre", usuarioForm.getNombre());
            }

            redirectAttributes.addFlashAttribute("mensaje", "Perfil actualizado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el perfil: " + e.getMessage());
        }

        return "redirect:/perfil";
    }
}