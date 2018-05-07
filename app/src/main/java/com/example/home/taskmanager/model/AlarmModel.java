package com.example.home.taskmanager.model;

import com.orm.SugarRecord;

public class AlarmModel extends SugarRecord {

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    private long time;
    private int status;
    private String message;
    private boolean sound;
}
