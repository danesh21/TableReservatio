package com.example.tablereservation;

import static com.example.tablereservation.MainActivity.redirectActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditReservation extends AppCompatActivity {

    private EditText etCustomerNameEdit;
    private EditText etCustomerPhoneNumberEdit;
    private Spinner spinnerMeal;
    private EditText etSeatingAreaEdit;
    private Spinner spinnerNoOfPax;
    private EditText editTextDate;
    private TextView cust;

    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation);

        // Initialize your UI elements

        etCustomerNameEdit = findViewById(R.id.etCustomerNameEdit);
        etCustomerPhoneNumberEdit = findViewById(R.id.etCustomerPhoneNumberEdit);
        spinnerMeal = findViewById(R.id.spinnerMeal);
        etSeatingAreaEdit = findViewById(R.id.etSeatingAreaEdit);
        spinnerNoOfPax = findViewById(R.id.spinnerNoOfPax);
        editTextDate = findViewById(R.id.editTextDate);
        btnBack=findViewById(R.id.backButton);

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(EditReservation.this, MyBooking.class);
            }
        });



        SharedPreferences preferences = getSharedPreferences("selected_reservation", MODE_PRIVATE);
        
        String selectedReservation = preferences.getString("selectedReservation", "");

        try {
            // Parse the selected reservation JSON data
            JSONArray jsonArray = new JSONArray(selectedReservation);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            // Extract values and set them in the respective EditText fields
            etCustomerNameEdit.setText(jsonObject.optString("customerName", ""));
            etCustomerPhoneNumberEdit.setText(jsonObject.optString("customerPhoneNumber", ""));
            // Set other fields accordingly

        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing errors
            Toast.makeText(this, "EDIT DATA ", Toast.LENGTH_SHORT).show();
        }
    }
    private void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

}
