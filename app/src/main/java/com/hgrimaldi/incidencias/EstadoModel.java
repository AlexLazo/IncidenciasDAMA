package com.hgrimaldi.incidencias;

public class EstadoModel {
    private int id_estadoEmpleado;

    private String Estado;

    public EstadoModel(int id_estadoEmpleado, String estado) {
        this.id_estadoEmpleado = id_estadoEmpleado;
        this.Estado = estado;
    }

    public int getId_estadoEmpleado() {
        return id_estadoEmpleado;
    }

    public void setId_estadoEmpleado(int id_estadoEmpleado) {
        this.id_estadoEmpleado = id_estadoEmpleado;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        this.Estado = estado;
    }
}
