package com.example.freelanceclone;

public class User {
    String Name;
    String Status;
    String Sex;
    String UID;

    public User(String name, String status, String sex, String UID) {
        this.Name = name;
        Status = status;
        Sex = sex;
        this.UID = UID;
    }

    public User() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
