package com.qbra.micromobil.model;

import com.google.gson.annotations.SerializedName;

public class PostResponse {
    @SerializedName("field1")
    private String Sicaklik;

    public String getMessage() {
        return Sicaklik;
    }

    public void setMessage(String gonderilenSicaklik) {
        this.Sicaklik = gonderilenSicaklik;
    }
}
