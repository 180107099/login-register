package com.example.instagram1.model;

public class likedPosts {
    private String Post;
    private String username1;
    private String username2;


    public likedPosts() {

    }

    public String getPost() {
        return Post;
    }

    public void setPost(String post) {
        Post = post;
    }

    public String getUsername1() {
        return username1;
    }

    public void setUsername1(String username1) {
        this.username1 = username1;
    }

    public String getUsername2() {
        return username2;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }

    public likedPosts(String Post, String username1, String username2) {
        this.Post = Post;
        this.username1 = username1;
        this.username2 = username2;

    }



}
