package com.example.studywithtimer.dataclass;

public class TimerItem {

    String date;
    String elapsedTime;
    String startTime;
    String endTime;

    public TimerItem(String date, String elapsedTime, String startTime, String endTime) {
        this.date = date;
        this.elapsedTime = elapsedTime;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getDate() {
        return date;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

}