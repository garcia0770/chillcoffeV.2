package com.ChillCoffeV1_4; // Asegúrate de usar el paquete base de tu aplicación

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashGenerator {

    public static void main(String[] args) {
        // Inicializa el codificador de contraseñas de Spring Security
        // Código Java para generar el hash de '123'
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String hash_123 = passwordEncoder.encode("123");
    System.out.println(hash_123); 
    
    String hash_abc = passwordEncoder.encode("abc");
    System.out.println(hash_abc);// Asegúrate de copiar todo, incluyendo el '$' inicial
        
        System.out.println("\nCOPIA CUALQUIERA DE ESTOS HASHES para tu sentencia SQL.");
    }
}