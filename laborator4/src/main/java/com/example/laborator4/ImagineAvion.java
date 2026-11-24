package com.example.laborator4;

import android.graphics.Bitmap;

public class ImagineAvion {
    private String urlImagine;
    private String descriere;
    private String urlPagina;
    private Bitmap imagine;

    public ImagineAvion(String urlImagine, String descriere, String urlPagina) {
        this.urlImagine = urlImagine;
        this.descriere = descriere;
        this.urlPagina = urlPagina;
        this.imagine = null;
    }

    public String getUrlImagine() {
        return urlImagine;
    }

    public void setUrlImagine(String urlImagine) {
        this.urlImagine = urlImagine;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getUrlPagina() {
        return urlPagina;
    }

    public void setUrlPagina(String urlPagina) {
        this.urlPagina = urlPagina;
    }

    public Bitmap getImagine() {
        return imagine;
    }

    public void setImagine(Bitmap imagine) {
        this.imagine = imagine;
    }
}