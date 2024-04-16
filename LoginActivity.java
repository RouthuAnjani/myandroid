package com.example.kpitusers;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.userName);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.login_btn);

        // Initially disable the login button
        buttonLogin.setEnabled(false);

        // Add TextChangedListener to the username EditText
        editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Check if both fields have text entered
                checkFieldsForEmptyValues();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        // Add TextChangedListener to the password EditText
        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Check if both fields have text entered
                checkFieldsForEmptyValues();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Check if the username and password are correct
                if (username.equals("admin") && password.equals("admin123")) {
                    // If correct, navigate to EmployeesActivity
                    Intent intent = new Intent(LoginActivity.this, EmployeesActivity.class);
                    startActivity(intent);
                    finish(); // Finish the LoginActivity so user can't navigate back to it
                } else {
                    // If incorrect, display an error message
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkFieldsForEmptyValues() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Enable the login button if both fields have text; otherwise, disable it
        if (!username.isEmpty() && !password.isEmpty()) {
            buttonLogin.setEnabled(true);
        } else {
            buttonLogin.setEnabled(false);
        }
    }
}
