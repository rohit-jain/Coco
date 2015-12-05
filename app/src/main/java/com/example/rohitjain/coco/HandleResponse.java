package com.example.rohitjain.coco;

/**
 * Created by rohitjain on 12/10/15.
 */
public interface HandleResponse {
    void downloadComplete(String output);
    void removeFromTtsList(Boundary b);
    void setUsername(String output);
}