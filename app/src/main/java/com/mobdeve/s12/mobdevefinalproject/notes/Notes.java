package com.mobdeve.s12.mobdevefinalproject.notes;

import android.graphics.Color;

public class Notes {

    private String id;
    private String text;
    private int fontColor;
    private int backgroundColor;

    public Notes(String id, String text, int fontColor, int backgroundColor) {
        this.id = id;
        this.text = text;
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor;
    }

    public String getText() {
        return this.text;
    }

    public int getFontColor() {
        return this.fontColor;
    }

    public int getBackgoundColor() {
        return this.backgroundColor;
    }

    public String getNoteId() {
        return this.id;
    }
}
