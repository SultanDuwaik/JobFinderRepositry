package com.example.jobfinder.company.ui.home;

public class Companys {

    String image, title, jobRole;


    public  Companys(){}


    public Companys(String image, String title, String jobRole) {
        this.image = image;
        this.title = title;
        this.jobRole = jobRole;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }
}
