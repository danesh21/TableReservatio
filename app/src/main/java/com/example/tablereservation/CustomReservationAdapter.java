package com.example.tablereservation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CustomReservationAdapter extends ArrayAdapter<String> {

    private Context context;
    private int resource;
    private List<String> reservations;

    public CustomReservationAdapter(Context context, int resource, List<String> reservations) {
        super(context, resource, reservations);
        this.context = context;
        this.resource = resource;
        this.reservations = reservations;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
        }

        // Bind data to the list_reservation_item layout
        try {
            JSONObject jsonObject = new JSONArray(reservations.get(position)).getJSONObject(0);

            TextView textViewCustomerName = convertView.findViewById(R.id.textViewCustomerName);
            TextView textViewPhoneNumber = convertView.findViewById(R.id.textViewPhoneNumber);
            TextView textViewSeatingArea = convertView.findViewById(R.id.textViewSeatingArea);
            TextView textViewMeal = convertView.findViewById(R.id.textViewMeal);
            TextView textViewTableSize = convertView.findViewById(R.id.textViewTableSize);
            TextView textViewDate = convertView.findViewById(R.id.textViewDate);
            // Bind other TextViews similarly

            // Set text values based on the JSON data
            textViewCustomerName.setText("Customer Name: " + jsonObject.getString("customerName"));
            textViewPhoneNumber.setText("Phone Number:" +jsonObject.getString("customerPhoneNumber"));
            textViewMeal.setText("Meal:" +jsonObject.getString("meal"));
            textViewSeatingArea.setText("Seating Area:" +jsonObject.getString("seatingArea"));
            textViewTableSize.setText("Table Size:" +jsonObject.getString("tableSize"));
            textViewDate.setText("Date:" +jsonObject.getString("date"));
            // Set other TextViews similarly

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}

