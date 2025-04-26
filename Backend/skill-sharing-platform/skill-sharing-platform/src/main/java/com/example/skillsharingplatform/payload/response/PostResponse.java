package com.example.skillsharingplatform.payload.response;

import java.util.Date;
import java.util.List;

import com.example.skillsharingplatform.model.Post;

public class PostResponse {
    private String id;
    private String userId;
    private String title;
    private String description;
    private List<String> mediaUrls;
    private Date createdAt;
    private Date updatedAt;
    private String authorName; // Will be populated from User service
    
    // Constructor from Post entity
    public PostResponse(Post post) {
        this.id = post.getId();
        this.userId = post.getUserId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.mediaUrls = post.getMediaUrls();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public List<String> getMediaUrls() {
        return mediaUrls;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public String getAuthorName() {
        return authorName;
    }
    
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}