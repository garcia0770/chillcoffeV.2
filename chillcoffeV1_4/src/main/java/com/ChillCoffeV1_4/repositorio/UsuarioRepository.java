package com.ChillCoffeV1_4.repositorio;

import com.ChillCoffeV1_4.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByNombre(String nombre);

    Usuario findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    Usuario findByResetToken(String token);

    Usuario findByResetToken(Usuario usuario2);

}