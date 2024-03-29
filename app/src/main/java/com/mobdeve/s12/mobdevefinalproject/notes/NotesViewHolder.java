package com.mobdeve.s12.mobdevefinalproject.notes;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s12.mobdevefinalproject.R;

import org.jetbrains.annotations.NotNull;

public class NotesViewHolder extends RecyclerView.ViewHolder {

    private LinearLayout llRvNotesTemplate;
    private TextView tvNotesText;
    private ConstraintLayout clNotesLayout;

    public NotesViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        this.tvNotesText = itemView.findViewById(R.id.tv_notes_text);
        this.clNotesLayout = itemView.findViewById(R.id.cl_notes_layout);
        this.llRvNotesTemplate = itemView.findViewById(R.id.ll_rv_notes_template);
    }

    public void setNotesText(String text) {
        this.tvNotesText.setText(text);
    }

    public void setBackgroundColor(int color) {
        this.clNotesLayout.setBackgroundColor(color);
    }

    public void setFontColor(int color) {
        this.tvNotesText.setTextColor(color);
    }

    public void setLlRvNotesTemplateColor(int color) {
        this.llRvNotesTemplate.setBackgroundColor(color);
    }
}
