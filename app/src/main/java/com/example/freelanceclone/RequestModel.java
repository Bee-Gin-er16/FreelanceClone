package com.example.freelanceclone;

public class RequestModel {
    String guest_name;
    String guest_id;

    public RequestModel(String guest_name, String guest_id) {
        this.guest_name = guest_name;
        this.guest_id = guest_id;
    }

    public RequestModel() {
    }

    public String getguest_name() {
        return guest_name;
    }

    public void setGuest_name(String guest_name) {
        this.guest_name = guest_name;
    }

    public String getguest_id() {
        return guest_id;
    }

    public void setGuest_id(String guest_id) {
        this.guest_id = guest_id;
    }
}
