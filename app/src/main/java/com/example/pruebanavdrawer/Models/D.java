package com.example.pruebanavdrawer.Models;

import com.example.pruebanavdrawer.Interfaces.IWsMessage;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class D implements IWsMessage {

    @SerializedName("topic")
    @Expose
    private String topic;
    @SerializedName("event")
    @Expose
    private String event;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
