package com.ChillCoffeV1_4.modelo;

import jakarta.persistence.*;


@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    private String nombre;
    private String correo;
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Rol rol;
    
    private String apellido;
    
    @Column(name = "reset_token")
    private String resetToken;
    
    // ⭐ CAMPOS AGREGADOS PARA CORREGIR EL ERROR DE COMPILACIÓN ⭐
    private String telefono;
    private String direccion;
    // ⭐ FIN CAMPOS AGREGADOS ⭐

    // Constructor, etc...
    public Usuario() {}

    // Getters y Setters existentes
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }
    
    // ⭐ GETTERS Y SETTERS AGREGADOS PARA CORREGIR EL ERROR DE COMPILACIÓN ⭐
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    // ⭐ FIN GETTERS Y SETTERS AGREGADOS ⭐

    public Object getIdUsuario() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getIdUsuario'");
    }

    public void setIdUsuario(Object idUsuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setIdUsuario'");
    }
}