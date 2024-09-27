package com.qbra.micromobil.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TemperatureHumidityResponse {
    @SerializedName("feeds")
    private List<Feeds> feeds;

    public List<Feeds> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feeds> feeds) {
        this.feeds = feeds;
    }
}
