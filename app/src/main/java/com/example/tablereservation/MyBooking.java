package com.example.tablereservation;

// Import statements

import static com.example.tablereservation.MainActivity.redirectActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class MyBooking extends AppCompatActivity {

        private ListView listView;
        private ArrayAdapter<String> adapter;
        private ArrayList<String> resultList;

        private ImageButton btnBack;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_my_booking);
            btnBack = findViewById(R.id.btnDirectMainPage);

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    redirectActivity(MyBooking.this, MainActivity.class);
                }
            });

            listView = findViewById(R.id.listView);

            resultList = new ArrayList<>();

            // Execute AsyncTask to fetch data from the API
            new FetchDataTask().execute("https://web.socem.plymouth.ac.uk/COMP2000/ReservationApi/api/Reservations");

            // Register a long click listener on the ListView items
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    showDeleteConfirmationDialog(position);
                    return true; // Consume the long click event
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Get the selected reservation data
                    String selectedReservation = resultList.get(position);

                    // Store the selected reservation data in SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("selected_reservation", MODE_PRIVATE);
                    String customerName = preferences.getString("customerName", "");
                    String customerPhoneNumber = preferences.getString("customerPhoneNumber", "");
                    String meal = preferences.getString("meal", "");
                    String seatingArea = preferences.getString("seatingArea", "");
                    String tableSize = preferences.getString("tableSize", "");
                    String date = preferences.getString("date", "");
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("selectedReservation", selectedReservation);
                    editor.apply();

                    // Start the EditReservation activity
                    Intent intent = new Intent(MyBooking.this, EditReservation.class);
                    startActivity(intent);
                }
            });
        }

    private class FetchDataTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            String apiUrl = params[0];
            ArrayList<String> resultList = new ArrayList<>();

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                reader.close();
                inputStream.close();
                connection.disconnect();

                String jsonResponse = stringBuilder.toString();

                // Parse JSON data
                JSONArray jsonArray = new JSONArray(jsonResponse);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    // Customize this part based on your JSON structure
                    String customerName = jsonObject.getString("customerName");
                    String customerPhoneNumber = jsonObject.getString("customerPhoneNumber");
                    String meal = jsonObject.getString("meal");
                    String seatingArea = jsonObject.getString("seatingArea");
                    String tableSize = jsonObject.getString("tableSize");
                    String date = jsonObject.getString("date");
                    resultList.add(" Name: " + customerName + "\n" +
                            "Phone Number: " + customerPhoneNumber + "\n" +
                            "Meal: " + meal + "\n" +
                            "Seating Area: " + seatingArea+ "\n" +
                            "Table Size: " + tableSize + "\n" +
                            "Date: " + date)
                    ;
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return resultList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);

            resultList = result;

            // Populate the ListView with the fetched data
            adapter = new ArrayAdapter<>(MyBooking.this, android.R.layout.simple_list_item_1, resultList);
            listView.setAdapter(adapter);
        }
    }



    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete this reservation?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User confirmed, delete the reservation
                        deleteReservation(position);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteReservation(int position) {
        // Get the reservation ID from the JSON data at the specified position
        try {
            JSONObject jsonObject = new JSONArray(resultList.get(position)).getJSONObject(0);
            int reservationId = jsonObject.getInt("id");

            // Construct the API endpoint for deleting the reservation
            String apiUrl = "https://web.socem.plymouth.ac.uk/COMP2000/ReservationApi/api/Reservations/" + reservationId;

            // Execute AsyncTask to delete data from the API
            new DeleteDataTask().execute(apiUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

        private class DeleteDataTask extends AsyncTask<String, Void, Boolean> {

            @Override
            protected Boolean doInBackground(String... params) {
                String apiUrl = params[0];

                try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("DELETE");

                    int responseCode = connection.getResponseCode();

                    // Check if the delete operation was successful (HTTP 204 No Content)
                    return responseCode == HttpURLConnection.HTTP_NO_CONTENT;

                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);

                if (success) {
                    Toast.makeText(MyBooking.this, "Reservation deleted", Toast.LENGTH_SHORT).show();
                    // Refresh the data from the API after a successful delete
                    new FetchDataTask().execute("https://web.socem.plymouth.ac.uk/COMP2000/ReservationApi/api/Reservations");
                } else {
                    Toast.makeText(MyBooking.this, "Failed to delete reservation", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
