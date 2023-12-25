package com.example.tablereservation;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Review extends AppCompatActivity {

    private EditText etDescription;
    private Spinner spinnerFood;
    private Spinner spinnerService;
    private Button btnSubmit;

    private DatabaseHelper dbHelper;

    private String selectedFoodRating;
    private String selectedServiceRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        dbHelper = new DatabaseHelper(this);

        etDescription = findViewById(R.id.atDescription);
        spinnerFood = findViewById(R.id.spinnerFood);
        spinnerService = findViewById(R.id.spinnerService);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Set up the spinners with options
        setupSpinners();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
    }

    private void setupSpinners() {
        // Spinner options for food rating
        List<String> foodRatingOptions = new ArrayList<>();
        foodRatingOptions.add("Great");
        foodRatingOptions.add("Normal");
        foodRatingOptions.add("Need Improvement");

        // Spinner options for service rating
        List<String> serviceRatingOptions = new ArrayList<>();
        serviceRatingOptions.add("On Time");
        serviceRatingOptions.add("Delayed");
        serviceRatingOptions.add("Late");

        // Create adapters for spinners
        ArrayAdapter<String> foodAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, foodRatingOptions);
        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, serviceRatingOptions);

        // Set dropdown views
        foodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapters to spinners
        spinnerFood.setAdapter(foodAdapter);
        spinnerService.setAdapter(serviceAdapter);

        // Spinner item click listeners
        spinnerFood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Store the selected food rating
                selectedFoodRating = foodRatingOptions.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Store the selected service rating
                selectedServiceRating = serviceRatingOptions.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    private void insertData() {

        String description = etDescription.getText().toString();
        String food = spinnerFood.getSelectedItem().toString();
        String service = spinnerService.getSelectedItem().toString();

        // Assuming you have a DatabaseHelper class with an insertBookingData method
        DatabaseHelper dbHelper = new DatabaseHelper(Review.this);

        // Attempt to insert data into the database
        boolean isInserted = dbHelper.insertReviewData(food, service, description);

        if (isInserted) {
            // Successful insertion, show success message
            showToast("Your review has been submitted successfully.Redirecting back to home screen.");
            // You can navigate to the next screen or perform other actions here
            Intent intent = new Intent(Review.this, BookNow.class);
            startActivity(intent);
        } else {
            // Handle insertion failure, show failure message
            showToast("Failed to insert booking data. Please try again.");
        }
    }


    // Utility method to show a toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
