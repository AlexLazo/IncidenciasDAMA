package com.hgrimaldi.incidencias;

public class TipoIncidenciaModel {
    int id_tipIncidencia;
    String TipoIncidencia;


    public TipoIncidenciaModel(int id_tipIncidencia, String tipoIncidencia) {
        this.id_tipIncidencia = id_tipIncidencia;
        TipoIncidencia = tipoIncidencia;
    }

    public int getId_tipIncidencia() {
        return id_tipIncidencia;
    }

    public void setId_tipIncidencia(int id_tipIncidencia) {
        this.id_tipIncidencia = id_tipIncidencia;
    }

    public String getTipoIncidencia() {
        return TipoIncidencia;
    }

    public void setTipoIncidencia(String tipoIncidencia) {
        TipoIncidencia = tipoIncidencia;
    }
}
