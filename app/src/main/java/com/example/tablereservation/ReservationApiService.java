package com.example.tablereservation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReservationApiService {
    @POST("reservation") // Specify the endpoint for reservation
    Call<Void> postReservation(@Body Reservation reservation);

    @GET("Reservations")
    Call<List<Reservation>> getReservation();

    @PUT("Reservations/{id}")
    Call<Void> updateHistoryItem(@Path("id") int historyItemId, @Body Reservation updatedHistoryItem);
}


