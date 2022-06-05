package com.cookandroid.blogappproject.Models;

import com.google.firebase.database.ServerValue;

public class Comment {

    private String content;
    private String userId;
    private String userPhoto;
    private String userName;
    private Object timeStamp;

    // Constructor
    public Comment(String content, String userId, String userPhoto, String userName) {
        this.content = content;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.userName = userName;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public Comment() {
    }

    // Getter
    public String getContent() {
        return content;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }
}
