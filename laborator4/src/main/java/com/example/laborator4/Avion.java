package com.example.laborator4;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Avion implements Parcelable {
    private String model;
    private boolean areMotorReactie;
    private String tipUtilizare;
    private String producator;
    private float rating;
    private boolean esteInServiciu;

    public Avion() {

    }

    public Avion(String _model, boolean _areMotorReactie, String _tipUtilizare, String _producator, float _rating, boolean _esteInServiciu) {
        this.model = _model;
        this.areMotorReactie = _areMotorReactie;
        this.tipUtilizare = _tipUtilizare;
        this.producator = _producator;
        this.rating = _rating;
        this.esteInServiciu = _esteInServiciu;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean getAreMotorReactie() {
        return areMotorReactie;
    }

    public void setAreMotorReactie(boolean areMotorReactie) {
        this.areMotorReactie = areMotorReactie;
    }

    public String getTipUtilizare() {
        return tipUtilizare;
    }

    public void setTipUtilizare(String tipUtilizare) {
        this.tipUtilizare = tipUtilizare;
    }

    public String getProducator() {
        return producator;
    }

    public void setProducator(String producator) {
        this.producator = producator;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isEsteInServiciu() {
        return esteInServiciu;
    }

    public void setEsteInServiciu(boolean esteInServiciu) {
        this.esteInServiciu = esteInServiciu;
    }

    protected Avion(Parcel in) {
        model = in.readString();
        areMotorReactie = in.readByte() != 0; // boolean se citeste ca byte
        tipUtilizare = in.readString();
        producator = in.readString();
        rating = in.readFloat();
        esteInServiciu = in.readByte() != 0;
    }

    public static final Creator<Avion> CREATOR = new Creator<Avion>() {
        @Override
        public Avion createFromParcel(Parcel in) {
            return new Avion(in);
        }

        @Override
        public Avion[] newArray(int size) {
            return new Avion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    //aici se scriu atributele in pachet
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(model);
        dest.writeByte((byte) (areMotorReactie ? 1 : 0));
        dest.writeString(tipUtilizare);
        dest.writeString(producator);
        dest.writeFloat(rating);
        dest.writeByte((byte) (esteInServiciu ? 1 : 0));
    }

    @Override
    public String toString() {
        return "Avion: " + model +
                "\nProducător: " + producator +
                "\nUtilizare: " + tipUtilizare +
                "\nMotor cu reacție: " + (areMotorReactie ? "Da" : "Nu") +
                "\nÎn serviciu: " + (esteInServiciu ? "Da" : "Nu") +
                "\nRating: " + rating;
    }
}