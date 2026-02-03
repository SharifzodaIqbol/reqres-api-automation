package org.example.reqres.tests;

import okhttp3.OkHttpClient;
import org.example.reqres.api.ApiKeyInterceptor;
import org.example.reqres.api.ReqresApi;
import org.example.reqres.models.UserRequest;
import org.example.reqres.models.UserResponse;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RegistrationTest {
    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new ApiKeyInterceptor())
            .build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://reqres.in/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private final ReqresApi api = retrofit.create(ReqresApi.class);
    @Test
    public void testSuccessfulRegistration() throws IOException{
        UserRequest request = new UserRequest("eve.holt@reqres.in", "pistol");
        Response<UserResponse> response = api.registerUser(request).execute();
        System.out.println("Response code: " + response.code());
        if (response.code() == 403) {
            System.out.println("Error body: " + response.errorBody().string());
        }
        assertThat(response.code()).isEqualTo(200);
        assertThat(response.body()).isNotNull();
        assertThat(response.body().getToken()).isNotEmpty();
        assertThat(response.body().getId()).isPositive();
    }
}
