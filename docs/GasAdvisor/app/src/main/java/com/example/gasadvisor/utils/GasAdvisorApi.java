package com.example.gasadvisor.utils;

import com.example.gasadvisor.model.Prezzo;
import com.example.gasadvisor.model.User;
import com.example.gasadvisor.model.Valutazione;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GasAdvisorApi {
    @POST("login")
    @FormUrlEncoded
    Call<User> getUserLogin(@Field("username") String username,
                            @Field("password") String password);

    @POST("AggiungiValutazione")
    @FormUrlEncoded
    Call<Valutazione> aggiungiValutazione(@FieldMap Map<String, String> params);

    @GET("AggiornaMediaValutazioni")
    Call<Map<Integer, Float>> getMediaRecensioni();

    @GET("cercaPiuEconomici")
    Call<List<Prezzo>> getPrezziPiuEconomici(@Query("carburante") String carburante);
}
