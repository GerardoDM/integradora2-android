package com.example.pruebanavdrawer.Models;

import com.example.pruebanavdrawer.Interfaces.IWsMessage;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example implements IWsMessage {

    @SerializedName("t")
    @Expose
    private Integer t;
    @SerializedName("d")
    @Expose
    private D d;

    public Integer getT() {
        return t;
    }

    public void setT(Integer t) {
        this.t = t;
    }

    public D getD() {
        return d;
    }

    public void setD(D d) {
        this.d = d;
    }
}
