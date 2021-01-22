package com.example.capstoneblackbox;

import android.graphics.Bitmap;

public class Video {
    private Bitmap videoResource;
    private String videoName;
    private int playButton;
    private String duration;

    public Video(Bitmap videoResource, String videoName, int playButton, String duration) {
        this.videoResource = videoResource;
        this.videoName = videoName;
        this.playButton = playButton;
        this.duration = duration;
    }

    public Bitmap getVideoResource() {
        return videoResource;
    }

    public void setVideoResource(Bitmap videoResource) {
        this.videoResource = videoResource;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public int getPlayButton() {
        return playButton;
    }

    public void setPlayButton(int playButton) {
        this.playButton = playButton;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
