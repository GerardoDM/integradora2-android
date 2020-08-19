package com.example.pruebanavdrawer.Models;

import com.example.pruebanavdrawer.Interfaces.IWsMessage;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements IWsMessage {

    @SerializedName("distance")
    @Expose
    private Double distance;

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
