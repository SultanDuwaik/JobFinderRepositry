package com.example.jobfinder;

public class CompanyList {

    String image;
    String companyName;
    String description;
    String companyId;
    String postId;

    public CompanyList(String image, String companyName, String description, String companyId) {
        this.image = image;
        this.companyName = companyName;
        this.description = description;
        this.companyId = companyId;
    }

    public CompanyList(String image, String companyName, String description) {
        this.image = image;
        this.companyName = companyName;
        this.description = description;
    }

    public CompanyList(String image, String companyName, String description, String companyId , String postId) {
        this.image = image;
        this.companyName = companyName;
        this.description = description;
        this.companyId = companyId;
        this.postId = postId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
