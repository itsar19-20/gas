package com.example.gasadvisor.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {
    //questo serve per fare il Parse della data dal DB
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
        public Date deserialize(JsonElement json, Type typeofT, JsonDeserializationContext context) throws JsonParseException {
            return new Date(json.getAsLong());
        }
    }).create();

    private static RetrofitUtils instance = null;
    public static final String BASE_URL = "http://192.168.1.6:8080/gas/";
    private GasAdvisorApi gasAdvisorApi;
    public static RetrofitUtils getInstance() {
        if (instance == null) {
            instance = new RetrofitUtils();
        }
        return instance;
    }
    private RetrofitUtils() {

        // Implement a method to build your retrofit
        buildRetrofit();
    }
    private void buildRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Build your services once
        this.gasAdvisorApi = retrofit.create(GasAdvisorApi.class);
    }
    public GasAdvisorApi getGasAdvisorApi() {
        return this.gasAdvisorApi;
    }
}
