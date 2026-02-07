package org.example.reqres.tests;

import okhttp3.OkHttpClient;
import org.example.reqres.api.ApiKeyInterceptor;
import org.example.reqres.api.ReqresApi;
import org.junit.jupiter.api.BeforeEach;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseTest {

    protected ReqresApi api;

    @BeforeEach
    void setup() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ApiKeyInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://reqres.in/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(ReqresApi.class);
    }
}
