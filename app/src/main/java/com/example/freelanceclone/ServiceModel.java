package com.example.freelanceclone;

public class ServiceModel {
    String Poster;
    String Service;
    String Min;
    String Max;
    String UID;

    public ServiceModel(){

    }

    public ServiceModel(String poster, String service, String pay_min, String pay_max, String id) {
        this.Poster = poster;
        this.Service = service;
        this.Min = pay_min;
        this.Max = pay_max;
        this.UID = id;
    }

    public String getUID() {
        return UID;
    }

    public void setId(String id) {
        this.UID = id;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        this.Service = service;
    }

    public String getMin() {
        return Min;
    }

    public void setPay_min(String pay_min) {
        this.Min = pay_min;
    }

    public String getMax() {
        return Max;
    }

    public void setPay_max(String pay_max) {
        this.Max = pay_max;
    }
}
