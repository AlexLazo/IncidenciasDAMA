package com.hgrimaldi.incidencias;

public class RolModel {

    private int id_rol;
    private String Rol;

    public RolModel(int id_rol, String rol) {
        this.id_rol = id_rol;
        Rol = rol;
    }

    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }

    public String getRol() {
        return Rol;
    }

    public void setRol(String rol) {
        Rol = rol;
    }
}
