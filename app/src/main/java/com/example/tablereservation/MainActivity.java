package com.example.tablereservation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnBookNow, btnMyBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnBookNow=findViewById(R.id.btnBookNow);
        btnMyBooking=findViewById(R.id.btnMyBooking);


    }
}