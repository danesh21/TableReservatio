package com.example.tablereservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class BookNow extends AppCompatActivity {

     private ImageButton btnInside,btnGardenView,btnSeaView,btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);

         btnInside=findViewById(R.id.btnInside);
         btnGardenView=findViewById(R.id.btnGardenView);
         btnSeaView=findViewById(R.id.btnSeaView);
         btnBack=findViewById(R.id.btnBackBookNow);

         btnBack.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 redirectBackToMain();
             }
         });
         btnInside.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 openBookingInside();
             }
         });

         btnGardenView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 openActivityGardenBooking();
             }
         });

         btnSeaView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 openActivitySeaViewBooking();
             }
         });
    }
    public void openBookingInside(){
        Intent intent=new Intent(this,InsideRestaurant.class);
        startActivity(intent);
    }

    public void openActivityGardenBooking(){
        Intent intentG=new Intent(this,GardenView.class);
        startActivity(intentG);
    }

    public void openActivitySeaViewBooking(){
        Intent intentS=new Intent(this, SeaView.class);
        startActivity(intentS);
    }
    public void redirectBackToMain(){
        Intent intentHome=new Intent(this,MainActivity.class);
        startActivity(intentHome);
    }
}