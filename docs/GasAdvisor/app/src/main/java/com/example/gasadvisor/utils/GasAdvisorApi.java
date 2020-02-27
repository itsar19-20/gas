package com.example.gasadvisor.utils;

import com.example.gasadvisor.model.Prezzo;
import com.example.gasadvisor.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GasAdvisorApi {
    @POST("login")
    @FormUrlEncoded
    Call<User> getUserLogin(@Field("username") String username,
                            @Field("password") String password);

    @GET("cercaPiuEconomici")
    Call<List<Prezzo>> getPrezziPiuEconomici(@Query("carburante") String carburante);
}
