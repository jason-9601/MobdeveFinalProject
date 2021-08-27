package com.mobdeve.s12.mobdevefinalproject.todo;

import java.sql.Time;

public class ToDo {
    private String todo_Title;
    private String todo_date;

    private String todo_time;

    private int priority;

    private boolean isSpecificTime;
    private boolean isNotified;

    private String remind_interval;
    private String remind_starting_time;

    public ToDo(String todo_Title, String todo_date, String todo_time, int priority, boolean isNotified,
                String remind_interval, String remind_starting_time, boolean isSpecificTime){
        this.todo_Title = todo_Title;
        this.todo_date = todo_date;
        this.todo_time = todo_time;
        this.priority = priority;

        this.isNotified = isNotified;
        this.remind_interval = remind_interval;
        this.remind_starting_time = remind_starting_time;

        this.isSpecificTime = isSpecificTime;
    }

    public String getTodo_Title(){
        return this.todo_Title;
    }

    public String getTodo_date(){
        return this.todo_date;
    }

    public String getTodo_time(){
        return this.todo_time;
    }

    public int getPriority(){
        return this.priority;
    }

    public boolean getIsNotified(){
        return this.isNotified;
    }

    public String getRemindInterval()
    {
        return this.remind_interval;
    }

    public String getRemindStartingTime()
    {
        return this.remind_starting_time;
    }

    public void setNotified(boolean isNotified){
        this.isNotified = isNotified;
    }

    public boolean getIsSpecificTime()
    {
        return this.getIsSpecificTime();
    }
}
