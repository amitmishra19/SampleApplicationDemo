package com.example.codingtest.retrofit;

/**
 * Interface class to receive retrofit callbacks
 */
public interface Callback<T> {
    void onResponse(T response);

    void onError(String error);
}
