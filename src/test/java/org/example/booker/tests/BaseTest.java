package org.example.booker.tests;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.example.booker.api.BookingApi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {

    protected BookingApi api;

    @BeforeAll
    void setup() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json")
                            .build();
                    return chain.proceed(request);
                })
                .build();

        Gson gson = Converters.registerAll(new GsonBuilder())
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://restful-booker.herokuapp.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(BookingApi.class);
    }
}