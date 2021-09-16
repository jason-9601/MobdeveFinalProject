package com.mobdeve.s12.mobdevefinalproject.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s12.mobdevefinalproject.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    private ArrayList<Notes> notesList;

    public NotesAdapter(ArrayList<Notes> notesList) {
        this.notesList = notesList;
    }

    @NonNull
    @NotNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.rv_template_add_notes, parent, false);
        NotesViewHolder vh = new NotesViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NotesViewHolder holder, int i) {
        holder.setNotesText(this.notesList.get(i).getText());
        holder.setBackgroundColor(this.notesList.get(i).getBackgoundColor());
        holder.setLlRvNotesTemplateColor(this.notesList.get(i).getBackgoundColor());
        holder.setFontColor(this.notesList.get(i).getFontColor());
    }

    @Override
    public int getItemCount() {
        return this.notesList.size();
    }
}
