package com.example.codingtest.retrofit;

import com.example.codingtest.beans.DataModel;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Interface class to declare web APIs that are available to this application
 */
public interface APIService {
    String BASE_URL = "https://dl.dropboxusercontent.com";

    @GET("/u/746330/facts.json")
    Call<DataModel> getFacts();
}
