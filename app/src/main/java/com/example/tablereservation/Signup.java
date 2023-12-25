package com.example.tablereservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Signup extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword, editTextConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize the EditTexts
        editTextUsername = findViewById(R.id.signupUsern);
        editTextPassword = findViewById(R.id.signupPassword);
        editTextConfirmPassword = findViewById(R.id.confirmPassword);

        // Set up the "Sign up" button click listener
        Button btnSignup = findViewById(R.id.btn_signup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });

        // Set up the "Already a user ? Login here" text click listener
        TextView textViewDirectLogin = findViewById(R.id.directlogin);
        textViewDirectLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });
    }

    // Method to handle "Sign up" button click
    public void onSignupButtonClick(View view) {
        signUpUser();
    }

    // Method to handle "Already a user ? Login here" text click
    public void onDirectLoginClick(View view) {
        navigateToLogin();
    }

    // Method to sign up a new user
    private void signUpUser() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Validate username, password, and confirm password (add more validation if needed)
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showToast("Please enter all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showToast("Password and Confirm Password do not match.");
            return;
        }

        // Assuming you have a DatabaseHelper class with an insertUserData method
        DatabaseHelper dbHelper = new DatabaseHelper(Signup.this);

        // Check if the username already exists
        if (dbHelper.checkUsername(username)) {
            showToast("This username is already taken. Please choose a different username.");
            return;
        }

        // Attempt to insert user data into the database
        boolean isInserted = dbHelper.insertUserData(username, password);

        if (isInserted) {
            // Successful registration, you can navigate to the booking screen or perform other actions
            showToast("User Registration Successful. Redirecting back to login page .");
            Intent intent = new Intent(Signup.this, Login.class);
            // Pass the username to the next activity
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        } else {
            // Handle registration failure
            // You might want to show a message to the user or take appropriate action
            showToast("Failed to register user. Please try again.");
        }
    }

    // Method to navigate to the login activity
    private void navigateToLogin() {
        Intent intent = new Intent(Signup.this, Login.class);
        startActivity(intent);
    }

    // Utility method to show a toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
