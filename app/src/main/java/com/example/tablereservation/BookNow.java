package com.example.tablereservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class BookNow extends AppCompatActivity {

     private ImageButton btnInside;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);

         btnInside=findViewById(R.id.btnInside);
         btnInside.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 openBookingInside();
             }
         });
    }
    public void openBookingInside(){
        Intent intent=new Intent(this,SeaView.class);
        startActivity(intent);
    }
}