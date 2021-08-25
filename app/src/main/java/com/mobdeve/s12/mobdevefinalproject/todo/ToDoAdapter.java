package com.mobdeve.s12.mobdevefinalproject.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s12.mobdevefinalproject.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoViewHolder> {

    private ArrayList<ToDo> list;

    @NonNull
    @NotNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.rv_template_add_todo, parent, false);
        ToDoViewHolder vh = new ToDoViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ToDoViewHolder holder, int position) {
        holder.setTitle(list.get(position).getTodo_Title());
        holder.setDate(list.get(position).getTodo_date());
        holder.setTime(list.get(position).getTodo_time().getTimeString());
        holder.setPriority(list.get(position).getPriority());

        holder.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isOn = !list.get(position).getIsNotified();
                holder.setNotifications(isOn, view);
                list.get(position).setNotified(isOn);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }
}
