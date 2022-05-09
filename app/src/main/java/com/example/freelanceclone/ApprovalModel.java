package com.example.freelanceclone;

import java.sql.Timestamp;

public class ApprovalModel {
    String Job_id;
    Boolean Access;
    String Job_title;

    public ApprovalModel() {
    }

    public ApprovalModel(String job_id,String job_title, Boolean access) {
        Job_id = job_id;
        Access = access;
        Job_title = job_title;
    }

    public String getJob_title() {
        return Job_title;
    }

    public void setJob_title(String job_title) {
        Job_title = job_title;
    }

    public String getJob_id() {
        return Job_id;
    }

    public void setJob_id(String job_id) {
        Job_id = job_id;
    }

    public Boolean getAccess() {
        return Access;
    }

    public void setAccess(Boolean access) {
        Access = access;
    }
}
