package com.mobdeve.s12.mobdevefinalproject.todo;

import java.sql.Time;

public class ToDo {

    private String todo_id;
    private String todo_Title;
    private String todo_date;
    private String todo_time;
    private int priority;
    private boolean isSpecificTime;
    private int isNotified;
    private String remind_interval;
    private String remind_starting_time;
    private String todo_hour;
    private String todo_minutes;
    private String backgroundColor;

    public ToDo(String todo_id, String todo_Title, String todo_date, String todo_time, int priority, int isNotified,
                String remind_interval, String remind_starting_time, boolean isSpecificTime,
                String todo_hour, String todo_minutes, String backgroundColor) {
        this.todo_id = todo_id;
        this.todo_Title = todo_Title;
        this.todo_date = todo_date;
        this.todo_time = todo_time;
        this.priority = priority;
        this.isNotified = isNotified;
        this.remind_interval = remind_interval;
        this.remind_starting_time = remind_starting_time;
        this.isSpecificTime = isSpecificTime;
        this.todo_hour = todo_hour;
        this.todo_minutes = todo_minutes;
        this.backgroundColor = backgroundColor;
    }

    public String getTodo_id() {
        return this.todo_id;
    }

    public String getTodo_Title() {
        return this.todo_Title;
    }

    public String getTodo_date() {
        return this.todo_date;
    }

    public String getTodo_time() {
        return this.todo_time;
    }

    public int getPriority() {
        return this.priority;
    }

    public int getIsNotified() {
        return this.isNotified;
    }

    public String getRemindInterval() {
        return this.remind_interval;
    }

    public String getRemindStartingTime() {
        return this.remind_starting_time;
    }

    public void setNotified(int isNotified) {
        this.isNotified = isNotified;
    }

    public boolean getIsSpecificTime() {
        return this.getIsSpecificTime();
    }

    public String getTodo_hour() {
        return this.todo_hour;
    }

    public String getTodo_minutes() {
        return this.todo_minutes;
    }

    public String getBackgroundColor() {
        return this.backgroundColor;
    }

}
