package com.example.linnersui;

import android.widget.ImageButton;

public class ModeloParadaFavorita {

    public Integer idParadaFavorita;
    public String direccion;
    public String localidadProvincia;
//    public ImageButton eliminarParadaFavo;

    public ModeloParadaFavorita(Integer idParadaFavorita, String direccion, String localidad) {
        this.idParadaFavorita = idParadaFavorita;
        this.direccion = direccion;
        this.localidadProvincia = localidad;
//        this.eliminarParadaFavo = eliminarParadaFavo;
    }


    public Integer getIdParadaFavorita() {
        return idParadaFavorita;
    }

    public void setIdParadaFavorita(Integer idParadaFavorita) {
        this.idParadaFavorita = idParadaFavorita;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLocalidadProvincia() {
        return localidadProvincia;
    }

    public void setLocalidadProvincia(String localidadProvincia) {
        this.localidadProvincia = localidadProvincia;
    }

//    public ImageButton getEliminarParadaFavo() {
//        return eliminarParadaFavo;
//    }
//
//    public void setEliminarParadaFavo(ImageButton eliminarParadaFavo) {
//        this.eliminarParadaFavo = eliminarParadaFavo;
//    }
}
