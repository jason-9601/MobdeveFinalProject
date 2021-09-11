package com.mobdeve.s12.mobdevefinalproject.todo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s12.mobdevefinalproject.R;
import com.mobdeve.s12.mobdevefinalproject.database.DatabaseHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoViewHolder> {

    private ArrayList<ToDo> list;
    private DatabaseHelper dbHelper;

    public ToDoAdapter(ArrayList<ToDo> list){
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        dbHelper = new DatabaseHelper(parent.getContext());
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.rv_template_add_todo, parent, false);
        ToDoViewHolder vh = new ToDoViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ToDoViewHolder holder, int position) {
        holder.setTitle(list.get(position).getTodo_Title());
        holder.setDate(list.get(position).getTodo_date());
        holder.setTime(list.get(position).getTodo_time());
        holder.setPriority(list.get(position).getPriority());
        holder.setNotifications(list.get(position).getIsNotified());

        holder.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToDo selectedTodo = list.get(holder.getBindingAdapterPosition());

                int isOn;

                if (selectedTodo.getIsNotified() == 1) {
                    isOn = 0;
                } else {
                    isOn = 1;
                }

                Log.d("todoadapter", Integer.toString(holder.getBindingAdapterPosition()));
                Log.d("isOn", Integer.toString(isOn));

                // Changes color of button //
                holder.setNotifications(isOn);

                // To Do model class //
                selectedTodo.setNotified(isOn);

                dbHelper.setToDoReminder(selectedTodo.getTodo_id(), isOn);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }
}
