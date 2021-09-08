package com.mobdeve.s12.mobdevefinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobdeve.s12.mobdevefinalproject.database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    private EditText etRegisterUsername;
    private EditText etRegisterPassword;
    private EditText etRegisterEmail;
    private Button btnRegisterSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(RegisterActivity.this);

        etRegisterUsername = findViewById(R.id.et_register_username);
        etRegisterPassword = findViewById(R.id.et_register_password);
        etRegisterEmail = findViewById(R.id.et_register_email);

        btnRegisterSubmit = findViewById(R.id.btn_register_submit);
        btnRegisterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper = new DatabaseHelper(RegisterActivity.this);

                if (inputIsNotComplete()) {
                    Toast.makeText(getApplicationContext(), "Please complete fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (invalidInputs()) {

                    } else {
                        dbHelper.addUser(etRegisterUsername.getText().toString().trim(),
                                etRegisterPassword.getText().toString().trim(),
                                etRegisterEmail.getText().toString().trim());

                        // Go back to login activity if register is successful //
                        finish();
                    }
                }
            }
        });
    }

    // Return true if at least one of the input fields is empty //
    public boolean inputIsNotComplete() {
        if (etRegisterUsername.getText().toString().trim().length() == 0 ||
                etRegisterPassword.getText().toString().trim().length() == 0 ||
                etRegisterEmail.getText().toString().trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    // Return true if inputs are valid. Checks if user exists and if email is in a correct format //
    public boolean invalidInputs() {
        if (dbHelper.userExists(etRegisterUsername.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(etRegisterEmail.getText().toString().trim()).matches()) {
            Toast.makeText(RegisterActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }
}