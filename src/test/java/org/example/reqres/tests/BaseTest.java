package org.example.reqres.tests;

import okhttp3.OkHttpClient;
import org.example.reqres.api.ApiKeyInterceptor;
import org.example.reqres.api.ReqresApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseTest {
    protected final OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new ApiKeyInterceptor())
            .build();

    protected final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://reqres.in/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    protected final ReqresApi api = retrofit.create(ReqresApi.class);
}