package com.mobdeve.s12.mobdevefinalproject.todo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobdeve.s12.mobdevefinalproject.R;

import org.jetbrains.annotations.NotNull;

public class ToDoViewHolder extends RecyclerView.ViewHolder{

    private TextView title;
    private TextView date;
    private TextView time;
    private TextView priority;

    private FloatingActionButton reminders;
    private ConstraintLayout layout;

    public ToDoViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        this.title = itemView.findViewById(R.id.tv_todo_title);
        this.date  = itemView.findViewById(R.id.tv_todo_date);
        this.time  = itemView.findViewById(R.id.tv_todo_time);
        this.priority = itemView.findViewById(R.id.tv_priority);
        this.reminders = itemView.findViewById(R.id.fab_reminders);
        this.layout = itemView.findViewById(R.id.cl_todo);
    }

    public void setTitle(String title){
        this.title.setText(title);
    }

    public void setDate(String date){
        this.date.setText(date);
    }

    public void setTime(String time){
        this.time.setText(time);
    }

    public void setPriority(int priority){
        this.priority.setText(Integer.toString(priority));
    }

    public void setNotifications(boolean isOn, View view){
        if(isOn){
            reminders.setBackgroundTintList(ColorStateList.valueOf(view.getResources().getColor(R.color.red)));
        }
        else{
            reminders.setBackgroundTintList(ColorStateList.valueOf(view.getResources().getColor(R.color.white)));
        }
    }

    public FloatingActionButton getButton(){
        return this.reminders;
    }
}
