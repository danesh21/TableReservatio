package com.example.tablereservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnBookNow, btnMyBooking,btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnBookNow=findViewById(R.id.btnBookNow);
        btnMyBooking=findViewById(R.id.btnMyBooking);


        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityBookNow();
            }
        });

    }
    public void openActivityBookNow(){
        Intent intent=new Intent(this, BookNow.class);
        startActivity(intent);
    }
}