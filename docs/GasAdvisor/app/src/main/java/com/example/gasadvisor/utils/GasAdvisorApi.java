package com.example.gasadvisor.utils;

import com.example.gasadvisor.model.Prezzo;
import com.example.gasadvisor.model.User;
import com.example.gasadvisor.model.Valutazione;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GasAdvisorApi {
    @POST("LoginUserController")
    @FormUrlEncoded
    Call<User> getUserLogin(@Field("username") String username,
                            @Field("password") String password);

    @POST("RegistrazioneUtente")
    @FormUrlEncoded
    Call<User> postUserSignUp(@Field("username") String username,
                              @Field("password") String password,
                              @Field("email") String email,
                              @Field("name") String name,
                              @Field("lastname") String lastname);

    @POST("ChangePassword")
    @FormUrlEncoded
    Call<User> postChangePassword(@Field("username") String username,
                                  @Field("password") String password,
                                  @Field("newPassword") String newPassword);

    @POST("ChangeUsername")
    @FormUrlEncoded
    Call<User> postChangeUsername(@Field("username") String username,
                                  @Field("password") String password,
                                  @Field("newUsername") String newUsername);

    @POST("ForgotPassword")
    @FormUrlEncoded
    Call<User> userForgotPassword(@Field("email") String email);

    @POST("AggiungiValutazione")
    @FormUrlEncoded
    Call<Valutazione> aggiungiValutazione(@FieldMap Map<String, String> params);

    @GET("AggiornaMediaValutazioni")
    Call<Map<Integer, Float>> getMediaRecensioni();

    @GET("cercaPiuEconomici")
    Call<List<Prezzo>> getPrezziPiuEconomici(@Query("carburante") String carburante);

    @GET("deleteUser")
    Call<ResponseBody> deleteUser(@Query("deleteUsername") String Username);
}
