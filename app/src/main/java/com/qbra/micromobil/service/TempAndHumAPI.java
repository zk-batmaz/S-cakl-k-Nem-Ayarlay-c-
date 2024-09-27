package com.qbra.micromobil.service;

import com.qbra.micromobil.model.TemperatureHumidityResponse;
import com.qbra.micromobil.model.PostResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

public interface TempAndHumAPI {
    @GET("2565686/feeds.json?results=2")
    Call<TemperatureHumidityResponse> getData();

    @FormUrlEncoded
    @POST("update")
    Call<PostResponse> postData(
            @Field("api_key") String apiKey,
            @Field("field1") int tempValue,
            @Field("field2") int humValue
    );
}
