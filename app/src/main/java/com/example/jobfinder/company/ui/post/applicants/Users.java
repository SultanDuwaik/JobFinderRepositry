package com.example.jobfinder.company.ui.post.applicants;

public class Users {

    String name, image, jobTitle, postId,userId;
    public  Users(){}

    public Users(String name, String image, String jobTitle, String postId, String userId) {

        this.name = name;
        this.image = image;
        this.jobTitle = jobTitle;
        this.postId = postId;
        this.userId = userId;

    }

    public String getName() { return name;}

    public void setName(String name) { this.name = name; }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
