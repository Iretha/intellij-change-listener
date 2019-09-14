package com.smdev.plugins;

import java.util.Date;

public class Change {

    private String path;

    private Date startTime;
    private Date endTime;

    public Change(String path) {
        this.path = path;
        this.startTime = new Date();
        this.endTime = new Date();
    }

    public String getPath() {
        return path;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getChangeDuration(){
        if(this.endTime == null){
            return 0L;
        }
        return this.endTime.getTime() - this.startTime.getTime();
    }
}
