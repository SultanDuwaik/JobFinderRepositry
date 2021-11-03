package com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.notifications;


public class ModelClass {

    private String notificationImage;
    private String fromText;
    private String notificationContent;
    private String notificationTime;



    public ModelClass (String notificationImage ,String fromText, String notificationContent,String notificationTime)
    {
        this.notificationImage = notificationImage;
        this.fromText = fromText;
        this.notificationContent = notificationContent;
        this.notificationTime = notificationTime;
    }

    public String getNotificationImage() {
        return notificationImage;
    }

    public String getFromText() {
        return fromText;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public String getNotificationTime() {
        return notificationTime;
    }


}

