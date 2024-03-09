package com.example.social_media_using_blockchain.models;

public class VideoModel {
    String description;
    String userName;
    String media_url;
    String songName;
    //encapsulation
    public VideoModel(String description, String userName, String media_url, String songName) {
        this.description = description;
        this.userName = userName;
        this.media_url = media_url;
        this.songName = songName;
    }

    public String getDescription() {
        return description;
    }

    public String getUserName() {
        return userName;
    }

    public String getMedia_url() {
        return media_url;
    }

    public String getSongName() {
        return songName;
    }
}