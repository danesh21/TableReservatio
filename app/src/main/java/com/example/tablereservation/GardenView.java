package com.example.tablereservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GardenView extends AppCompatActivity {

    private EditText etCustomerName, etMeal, etCustomerPhoneNumber, etTableSize, etDate;
    private Button btnBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden_view);

        etCustomerName = findViewById(R.id.etCustomerName);
        etMeal = findViewById(R.id.etMeal);

        etCustomerPhoneNumber = findViewById(R.id.etCustomerPhoneNumber);
        etTableSize = findViewById(R.id.etTableSize);
        etDate = findViewById(R.id.etDate);
        btnBook = findViewById(R.id.btnBook);

        // Set OnClickListener for the Date EditText to show DatePickerDialog
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        // Set OnClickListener for the Book Button
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform POST request with user input
                saveReservationData();
            }
        });
    }

    // Method to show DatePickerDialog
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        etDate.setText(sdf.format(selectedDate.getTime()));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    // AsyncTask to perform the POST request in the background
    private void saveReservationData() {
        String customerName = etCustomerName.getText().toString();
        String meal = etMeal.getText().toString();
        String seatingArea = "Garden View Outside";
        String customerPhoneNumber = etCustomerPhoneNumber.getText().toString();
        String tableSize = etTableSize.getText().toString();
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
                                Toast.makeText(GardenView.this, "Reservation successful", Toast.LENGTH_SHORT).show();
                            }
                        });
                        // Redirect to DashboardActivity
                        Intent intent = new Intent(GardenView.this, BookNow.class);
                        startActivity(intent);
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GardenView.this, "Failed to make reservation. Please try again.", Toast.LENGTH_SHORT).show();
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
