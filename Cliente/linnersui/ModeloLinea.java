package com.example.linnersui;

import android.widget.ImageView;

public class ModeloLinea {

    public String origen;
    public String destino;
    public String precio;
    public int logoLinea;
    public int logoEmpresa;

    public ModeloLinea(String origen, String destino, String precio, int logoLinea, int logoEmpresa) {
        this.origen = origen;
        this.destino = destino;
        this.precio = precio;
        this.logoLinea = logoLinea;
        this.logoEmpresa = logoEmpresa;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public int getLogoLinea() {
        return logoLinea;
    }

    public void setLogoLinea(int logoLinea) {
        this.logoLinea = logoLinea;
    }

    public int getLogoEmpresa() {
        return logoEmpresa;
    }

    public void setLogoEmpresa(int logoEmpresa) {
        this.logoEmpresa = logoEmpresa;
    }
}
