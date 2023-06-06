package com.hgrimaldi.incidencias;

public class EstadoIncidenciaModel {
    int id_estadoIncidencia;
    String EstadoIncidencia;

    public EstadoIncidenciaModel(int id_estadoIncidencia, String estadoIncidencia) {
        this.id_estadoIncidencia = id_estadoIncidencia;
        EstadoIncidencia = estadoIncidencia;
    }

    public int getId_estadoIncidencia() {
        return id_estadoIncidencia;
    }

    public void setId_estadoIncidencia(int id_estadoIncidencia) {
        this.id_estadoIncidencia = id_estadoIncidencia;
    }

    public String getEstadoIncidencia() {
        return EstadoIncidencia;
    }

    public void setEstadoIncidencia(String estadoIncidencia) {
        EstadoIncidencia = estadoIncidencia;
    }
}
