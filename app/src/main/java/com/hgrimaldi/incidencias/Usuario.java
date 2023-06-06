package com.hgrimaldi.incidencias;

public class Usuario {
    private String id_usuario,nombrecompleto,user,password;
    private int id_estadoEmpleado,id_rol;

    public Usuario(String id_usuario, String nombrecompleto, String user, String password, String id_rol, String id_estadoEmpleado) {
        this.id_usuario = id_usuario;
        this.nombrecompleto = nombrecompleto;
        this.user = user;
        this.password = password;
        this.id_rol = Integer.parseInt(id_rol);
        this.id_estadoEmpleado = Integer.parseInt(id_estadoEmpleado);
    }

    public int getId_estadoEmpleado() {
        return id_estadoEmpleado;
    }

    public void setId_estadoEmpleado(int id_estadoEmpleado) {
        this.id_estadoEmpleado = id_estadoEmpleado;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombrecompleto() {
        return nombrecompleto;
    }

    public void setNombrecompleto(String nombrecompleto) {
        this.nombrecompleto = nombrecompleto;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }

}
