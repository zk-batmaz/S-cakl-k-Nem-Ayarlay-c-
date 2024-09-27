package com.qbra.micromobil.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Feeds implements Serializable {
    @SerializedName("field1")
    private String sicaklik;

    @SerializedName("field2")
    private String nem;

    @SerializedName("field3")
    private String sicaklik2;

    @SerializedName("field4")
    private String nem2;

    public String getSicaklik() {
        return sicaklik;
    }

    public void setSicaklik(String sicaklik) {
        this.sicaklik = sicaklik;
    }

    public String getNem() {
        return nem;
    }

    public void setNem(String nem) {
        this.nem = nem;
    }

    public String getSicaklik2() {
        return sicaklik2;
    }

    public void setSicaklik2(String sicaklik2) {
        this.sicaklik2 = sicaklik2;
    }

    public String getNem2() {
        return nem2;
    }

    public void setNem2(String nem2) {
        this.nem2 = nem2;
    }

    @Override
    public String toString() {
        return "Sıcaklık: " + sicaklik + ", Nem: " + nem + ", Sıcaklık 2: " + sicaklik2 + ", Nem 2: " + nem2;
    }
}