package com.mobdeve.s12.mobdevefinalproject.todo;

import java.sql.Time;

public class ToDo {
    private String todo_Title;
    private String todo_date;

    private TimeString todo_time;

    private int priority;

    private boolean isNotified;

    private int remind_interval;
    private int remind_starting_time;

    public ToDo(String todo_Title, String todo_date, TimeString todo_time, int priority, boolean isNotified,
                int remind_interval, int remind_starting_time){
        this.todo_Title = todo_Title;
        this.todo_date = todo_date;
        this.todo_time = todo_time;
        this.priority = priority;

        this.isNotified = isNotified;
        this.remind_interval = remind_interval;
        this.remind_starting_time = remind_starting_time;
    }

    public String getTodo_Title(){
        return this.todo_Title;
    }

    public String getTodo_date(){
        return this.todo_date;
    }

    public TimeString getTodo_time(){
        return this.todo_time;
    }

    public int getPriority(){
        return this.priority;
    }

    public boolean getIsNotified(){
        return this.isNotified;
    }

    public int getRemindInterval()
    {
        return this.remind_interval;
    }

    public int getRemindStartingTime()
    {
        return this.remind_starting_time;
    }

    public void setNotified(boolean isNotified){
        this.isNotified = isNotified;
    }
}
