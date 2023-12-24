package com.example.tablereservation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReservationApiService {
    @POST("reservation") // Specify the endpoint for reservation
    Call<Void> postReservation(@Body Reservation reservation);
}

