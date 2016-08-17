package com.example.codingtest.retrofit;

import com.example.codingtest.DemoApplication;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private static RetrofitHelper sHelper = new RetrofitHelper();

    private RetrofitHelper() {
    }

    public static RetrofitHelper newInstance() {
        return sHelper;
    }

    /**
     * returns the APIService which is used to access calls from that service
     *
     * @param baseUrl base url for the api
     * @return
     */
    public APIService getAPIService(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService service = retrofit.create(APIService.class);
        return service;
    }

    /**
     * it will make  asynchronous request and post the event to the EventBus
     *
     * @param call     call type used to request the data(we have to get call type form the apiservice interface)
     */
    public synchronized void sendRequest(Call call) {
        call.enqueue(new retrofit2.Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                DemoApplication.eventBus.post(response);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                DemoApplication.eventBus.post(t);
            }
        });
    }
}
