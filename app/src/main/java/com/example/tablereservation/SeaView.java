package com.example.tablereservation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SeaView extends AppCompatActivity {

    private EditText etCustomerName, etMeal, etSeatingArea, etCustomerPhoneNumber, etTableSize, etDate;
    private Button btnBook;

    private ReservationApiService reservationApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sea_view);

        // Initialize UI elements
        etCustomerName = findViewById(R.id.etCustomerName);
        etMeal = findViewById(R.id.etMeal);
        etSeatingArea = findViewById(R.id.atSeatingArea);
        etCustomerPhoneNumber = findViewById(R.id.etCustomerPhoneNumber);
        etTableSize = findViewById(R.id.etTableSize);
        etDate = findViewById(R.id.etDate);
        btnBook = findViewById(R.id.btnBook);

        // Set up Retrofit for making API requests
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://web.socem.plymouth.ac.uk/COMP2000/ReservationApi/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        reservationApiService = retrofit.create(ReservationApiService.class);

        // Set click listener for the date field
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        // Set click listener for the book button
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDataToApi();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void updateDateField(int year, int month, int day) {
        // Format the date as "YYYY-MM-DD"
        String formattedDate = String.format("%04d-%02d-%02d", year, month + 1, day);
        etDate.setText(formattedDate);
    }

    private void postDataToApi() {
        // Retrieve data from EditText fields
        String customerName = etCustomerName.getText().toString();
        String meal = etMeal.getText().toString();
        String seatingArea = etSeatingArea.getText().toString();
        String customerPhoneNumber = etCustomerPhoneNumber.getText().toString();
        String tableSize = etTableSize.getText().toString();
        String date = etDate.getText().toString();

        // Validate input data before posting to API
        if (customerName.isEmpty() || meal.isEmpty() || seatingArea.isEmpty() ||
                customerPhoneNumber.isEmpty() || tableSize.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a Reservation object with the input data
        Reservation reservation = new Reservation(customerName, customerPhoneNumber, meal, seatingArea, tableSize, date);

        // Make a network request to post data to the API
        Call<Void> call = reservationApiService.postReservation(reservation);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SeaView.this, "Reservation successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SeaView.this, "Failed to make reservation. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SeaView.this, "Failed to make reservation. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
