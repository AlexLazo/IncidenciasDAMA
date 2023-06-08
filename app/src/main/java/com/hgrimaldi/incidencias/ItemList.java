package com.hgrimaldi.incidencias;

import java.io.Serializable;

public class ItemList implements Serializable {
    private String TipoIncidencia;
    private String descripcion;
    private String imagenReferencia;
    private String fecha;
    private String user;
    private String EstadoIncidencia;

    public ItemList(String TipoIncidencia, String descripcion, String imagenReferencia
    , String fecha, String user, String EstadoIncidencia) {
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.imagenReferencia = imagenReferencia;
        this.user = user;
        this.TipoIncidencia = TipoIncidencia;
        this.EstadoIncidencia = EstadoIncidencia;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public String getFecha(){ return  fecha;}
    public String getImage() {
        return imagenReferencia;
    }

    public String getuser(){ return  user;}
    public String getTipo() {
        return TipoIncidencia;
    }
    public String getEstadoIncidencia(){ return  EstadoIncidencia;}
}
