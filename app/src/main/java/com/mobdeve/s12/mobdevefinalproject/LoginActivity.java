package com.mobdeve.s12.mobdevefinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etLoginUsername;
    private EditText etLoginPassword;
    private Button btnLoginSubmit;
    private Button btnLoginToRegisterActivity;

    // Shared preferences //
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(LoginActivity.this);
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        spEditor = this.sp.edit();

        etLoginUsername = findViewById(R.id.et_login_username);
        etLoginPassword = findViewById(R.id.et_login_password);

        btnLoginSubmit = findViewById(R.id.btn_login_submit);
        btnLoginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (inputIsNotComplete()) {
                    Toast.makeText(getApplicationContext(), "Please complete fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean loginResult = dbHelper.loginUser(etLoginUsername.getText().toString().trim(),
                            etLoginPassword.getText().toString().trim());

                    if (loginResult) {
                        // Put username in shared preferences if successful login //
                        spEditor.putString("KEY_USERNAME_LOGGED_IN", etLoginUsername.getText().toString().trim());
                        spEditor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        });

        btnLoginToRegisterActivity = findViewById(R.id.btn_login_to_register_activity);
        btnLoginToRegisterActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    // Return true if at least one of the input fields is empty //
    public boolean inputIsNotComplete() {
        if (etLoginUsername.getText().toString().trim().length() == 0 ||
                etLoginPassword.getText().toString().trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }
}