package com.example.freelanceclone;

public class JobModel {
    private String Job_title;
    private String Job_ID;
    private String Job_payout;
    private Boolean Job_finished = false;
    private String post_owner;
    private String owner;

    private JobModel(){
    }

    public JobModel(String job_title, String job_ID, String job_payout, String job_owner, String owner) {
        Job_title = job_title;
        Job_ID = job_ID;
        Job_payout = job_payout;
        post_owner = job_owner;
        this.owner = owner;
    }

    public String getowner() {
        return owner;
    }

    public void setowner(String owner) {
        this.owner = owner;
    }

    public String getpost_owner() {
        return post_owner;
    }

    public void setpost_owner(String post_owner) {
        this.post_owner = post_owner;
    }

    public Boolean isJob_finished() { return Job_finished; }

    public void setFinished(Boolean finished) { this.Job_finished = finished; }

    public String getJob_title() {
        return Job_title;
    }

    public void setJob_title(String job_title) {
        Job_title = job_title;
    }

    public String getJob_ID() {
        return Job_ID;
    }

    public void setJob_ID(String job_ID) {
        Job_ID = job_ID;
    }

    public String getJob_payout() {
        return Job_payout;
    }

    public void setJob_payout(String job_payout) {
        Job_payout = job_payout;
    }
}
