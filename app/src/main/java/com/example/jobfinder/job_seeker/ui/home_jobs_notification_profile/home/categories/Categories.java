package com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.home.categories;

public class Categories {
    String text;
    Integer image;
    public Categories(String text , Integer image)
    {
        this.text = text;
        this.image = image;
    }

    public void setText(String text){ this.text = text; }
    public String getText(){ return text; }

    public void setImage(Integer image){ this.image = image;}
    public Integer getImage(){ return image; }


}
