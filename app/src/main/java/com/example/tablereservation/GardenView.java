package com.example.tablereservation;

import static com.example.tablereservation.MainActivity.redirectActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
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
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GardenView extends AppCompatActivity {

    private EditText etCustomerName, etCustomerPhoneNumber, etDate;
    private Button btnBook;

    private Spinner spMealGarden,spTableSizeGarden;

    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden_view);

        etCustomerName = findViewById(R.id.etCustomerName);
        etCustomerPhoneNumber = findViewById(R.id.etCustomerPhoneNumber);
        etDate = findViewById(R.id.etDate);
        spMealGarden=findViewById(R.id.spMealGarden);
        spTableSizeGarden=findViewById(R.id.spTableSizeGarden);
        btnBook = findViewById(R.id.btnBook);
        btnBack=findViewById(R.id.btnBackGardenBook);

        setUpNoOfPaxSpinner();
        setUpMealPreferenceSpinner();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(GardenView.this, BookNow.class);
            }
        });

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

    private void setUpNoOfPaxSpinner() {
        List<String> paxList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            paxList.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, paxList
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTableSizeGarden.setAdapter(adapter);

        // Set a listener to handle item selection
        spTableSizeGarden.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        spMealGarden.setAdapter(adapter);

        // Set a listener to handle item selection
        spMealGarden.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        String seatingArea = "Garden View Outside";
        String customerPhoneNumber = etCustomerPhoneNumber.getText().toString();
        String meal = spMealGarden.getSelectedItem().toString();
        String tableSize = spTableSizeGarden.getSelectedItem().toString();
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
