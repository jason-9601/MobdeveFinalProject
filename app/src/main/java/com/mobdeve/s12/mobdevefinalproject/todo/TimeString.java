package com.mobdeve.s12.mobdevefinalproject.todo;

public class TimeString {
    private int hour;
    private int minute;
    private String ampm;

    public TimeString(int hour, int minute, String ampm){
        this.hour = hour;
        this.minute = minute;
        this.ampm = ampm;
    }

    public String getTimeString(){
        return this.hour + ":" + this.minute + " " + this.ampm;
    }

    public int getHour(){ return this.hour;}
    public int getMinute(){ return this.minute;}
    public String getAmpm(){ return this.ampm;}

    public void setHour(int hour){
        this.hour = hour;
    }

    public void setMinute(int minute){
        this.minute = minute;
    }

    public void setAmpm(String ampm){
        this.ampm = ampm;
    }

}
