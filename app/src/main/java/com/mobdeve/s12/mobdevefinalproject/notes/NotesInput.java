package com.mobdeve.s12.mobdevefinalproject.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobdeve.s12.mobdevefinalproject.R;
import com.mobdeve.s12.mobdevefinalproject.addexpenses.AddExpensesInput;
import com.mobdeve.s12.mobdevefinalproject.database.DatabaseHelper;

import yuku.ambilwarna.AmbilWarnaDialog;


public class NotesInput extends AppCompatActivity {

    private TextView notesInput;
    private Button backgroundColor;
    private Button fontColor;
    private Button button;

    private int backgroundColorInt;
    private int fontColorInt;

    private int defaultBackgroundColor;
    private int defaultFontColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes_input);

        this.notesInput      = findViewById(R.id.tv_notes_input);
        this.backgroundColor = findViewById(R.id.btn_background_color);
        this.fontColor       = findViewById(R.id.btn_font_color);
        this.button          = findViewById(R.id.btn_done);

        defaultBackgroundColor = Color.parseColor("#FFFFFF");
        defaultFontColor = Color.parseColor("#000000");

        backgroundColorInt = defaultBackgroundColor;
        fontColorInt = defaultFontColor;

        backgroundColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showColorPicker(true, backgroundColorInt);
            }
        });

        fontColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showColorPicker(false, fontColorInt);
            }
        });


        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                addToDatabase();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }

    private void showColorPicker(boolean key, int currColor){
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, currColor,
                new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {

                        if(key){
                            backgroundColorInt = color;
                            backgroundColor.setBackgroundColor(backgroundColorInt);
                        }
                        else{
                            fontColorInt = color;
                            fontColor.setTextColor(fontColorInt);
                        }
                    }
                });
        colorPicker.show();
    }

    private void addToDatabase(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor spEditor = sp.edit();

        // Print logged in user to Logcat for checking //
        String loggedInUser = sp.getString("KEY_USERNAME_LOGGED_IN", "N/A");
        Log.d("Logged in user: ", loggedInUser);

        DatabaseHelper dbHelper = new DatabaseHelper(NotesInput.this);
        dbHelper.addUserNote(notesInput.getText().toString(), backgroundColorInt,
                fontColorInt, loggedInUser);
    }
}

