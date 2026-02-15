package com.ChillCoffeV1_4.servicio;

import com.ChillCoffeV1_4.modelo.Rol;
import com.ChillCoffeV1_4.modelo.Usuario;
import com.ChillCoffeV1_4.repositorio.UsuarioRepository;
import jakarta.transaction.Transactional; // Importación necesaria para @Transactional

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    
    // ✅ CONSTRUCTOR CORREGIDO: Solo toma UsuarioRepository y BCryptPasswordEncoder
    public UsuarioService(UsuarioRepository usuarioRepository, 
                          BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ============================================================
    // 🔹 LOGIN & BUSQUEDA
    // ============================================================
    public Usuario buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre);
    }

    // ============================================================
    // 🔹 REGISTRO (Mantiene el encoding de password)
    // ============================================================
    public boolean correoExiste(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }

    public boolean registrarUsuario(Usuario usuario) {

        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            return false;
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        if (usuario.getRol() == null) {
            usuario.setRol(Rol.cliente);
        }

        usuarioRepository.save(usuario);
        return true;
    }

    // ⚠️ MÉTODO ORIGINAL PARA GUARDAR CON ENCRIPTACIÓN (Usado para Registro o Admin)
    public void guardarUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
    }
    
    // ✅ NUEVO MÉTODO PARA GUARDAR EL PERFIL (NO encripta la contraseña, solo actualiza)
    @Transactional
    public void actualizarPerfil(Usuario usuario) {
        // Asumimos que los campos sensibles (password, rol) ya fueron transferidos en el controlador
        // Solo guardamos la entidad para que JPA actualice los campos modificables (teléfono, dirección, correo, etc.)
        usuarioRepository.save(usuario);
    }


    // ============================================================
    // 🔥 RECUPERAR CONTRASEÑA
    // ============================================================

    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    // Generar token y guardarlo en el usuario
    // ... (El resto de los métodos de token están correctos y no se modificaron)
    // ...

    public String generarTokenRecuperacion(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo);

        if (usuario == null) {
            return null;
        }

        String token = UUID.randomUUID().toString();

        usuario.setResetToken(token);
        usuarioRepository.save(usuario);

        return token;
    }

    public boolean tokenValido(String token) {
        return usuarioRepository.findByResetToken(token) != null;
    }

    public Usuario getUsuarioPorToken(String token) {
        return usuarioRepository.findByResetToken(token);
    }

    /**
     * Cambia la contraseña y limpia el token.
     */
    public boolean actualizarContrasena(Usuario usuario, String nuevaClave) {

        if (usuario == null) {
            return false;
        }

        // Cifrar nueva contraseña
        usuario.setPassword(passwordEncoder.encode(nuevaClave));

        // Eliminar token
        usuario.setResetToken(null);

        usuarioRepository.save(usuario);

        return true;
    }

    /**
     * Simulación de envío de correo (temporalmente).
     */
    public void enviarCorreoRecuperacion(String correo, String token) {
        System.out.println("=========================================");
        System.out.println(" 📧 SIMULACIÓN DE ENVÍO DE CORREO");
        System.out.println(" Para: " + correo);
        System.out.println(" Token: " + token);
        System.out.println(" Enlace para restablecer:");
        System.out.println(" http://localhost:8080/cambiar?token=" + token);
        System.out.println("=========================================");
    }
}