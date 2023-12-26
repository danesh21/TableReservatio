package com.example.tablereservation;

import static com.example.tablereservation.MainActivity.redirectActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SeaView extends AppCompatActivity {

    private EditText etCustomerName, etMeal, etCustomerPhoneNumber, etTableSize, etDate;
    private Button btnBook;

    private ImageButton btnBack;

    private Spinner spMeal,spTableSize;

    private ReservationApiService reservationApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sea_view);

        // Initialize UI elements
        etCustomerName = findViewById(R.id.etCustomerName);
        etCustomerPhoneNumber = findViewById(R.id.etCustomerPhoneNumber);
        etDate = findViewById(R.id.etDate);
        btnBook = findViewById(R.id.btnBook);
        btnBack=findViewById(R.id.btnBackSeaBook);
        spMeal=findViewById(R.id.spMeal);
        spTableSize=findViewById(R.id.spTableSize);


        setUpNoOfPaxSpinner();
        setUpMealPreferenceSpinner();




        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(SeaView.this, BookNow.class);
            }
        });


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
                saveReservationData();
            }
        });
    }

    private void setUpNoOfPaxSpinner() {
        List<String> paxList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            paxList.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, paxList
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTableSize.setAdapter(adapter);

        // Set a listener to handle item selection
        spTableSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected number of pax
                String selectedPax = parentView.getItemAtPosition(position).toString();
                // You can use selectedPax as needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    // Method to set up the Spinner for selecting meal preference
    private void setUpMealPreferenceSpinner() {
        List<String> mealPreferences = new ArrayList<>();
        mealPreferences.add("Breakfast");
        mealPreferences.add("Lunch");
        mealPreferences.add("Dinner");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, mealPreferences
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMeal.setAdapter(adapter);

        // Set a listener to handle item selection
        spMeal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected meal preference
                String selectedMeal = parentView.getItemAtPosition(position).toString();
                // You can use selectedMeal as needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
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


    private void saveReservationData() {
        String customerName = etCustomerName.getText().toString();
        String seatingArea = "Sea View Outside";
        String customerPhoneNumber = etCustomerPhoneNumber.getText().toString();
        String meal = spMeal.getSelectedItem().toString();
        String tableSize = spTableSize.getSelectedItem().toString();
        String date = etDate.getText().toString();



        // Create a JSON object with the data
        JSONObject reservationData = new JSONObject();
        try {
            reservationData.put("customerName", customerName);
            reservationData.put("customerPhoneNumber", customerPhoneNumber);
            reservationData.put("meal", meal);
            reservationData.put("seatingArea", seatingArea);
            reservationData.put("tableSize", tableSize);
            reservationData.put("date", date);




        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Storing data in SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("ReservationData", MODE_PRIVATE).edit();
        editor.putString("customerName", customerName);
        editor.putString("customerPhoneNumber", customerPhoneNumber);
        editor.putString("meal", meal);
        editor.putString("seatingArea", seatingArea);
        editor.putString("tableSize", tableSize);
        editor.putString("date", date);





        editor.apply();

        // Perform the HTTP POST request
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://web.socem.plymouth.ac.uk/COMP2000/ReservationApi/api/Reservations");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    // Write data to the server
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(reservationData.toString());
                    writer.flush();
                    writer.close();
                    os.close();

                    // Get the response from the server (if needed)
                    int responseCode = conn.getResponseCode();
                    if (responseCode >= HttpURLConnection.HTTP_OK && responseCode < HttpURLConnection.HTTP_MULT_CHOICE) {
                        // Successful POST
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SeaView.this, "Reservation successful", Toast.LENGTH_SHORT).show();
                            }
                        });
                        // Redirect to DashboardActivity
                        Intent intent = new Intent(SeaView.this, BookNow.class);
                        startActivity(intent);
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SeaView.this, "Failed to make reservation. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
}
}
