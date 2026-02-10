package org.example.booker.tests;

import org.example.booker.ApiClient;
import org.example.booker.api.DefaultApi;
import org.example.booker.model.AuthRequest;
import org.example.booker.model.AuthResponse;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;

public class BaseTest {
    protected static DefaultApi api;
    protected static ApiClient apiClient;

    @BeforeAll
    static void setup() throws IOException {
        apiClient = new ApiClient();
        apiClient.getAdapterBuilder().baseUrl("https://restful-booker.herokuapp.com/");

        api = apiClient.createService(DefaultApi.class);

        authenticate();
    }

    private static void authenticate() throws IOException {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("admin");
        authRequest.setPassword("password123");

        var response = api.createToken(authRequest).execute();

        if (response.isSuccessful() && response.body() != null) {
            String token = response.body().getToken();

            apiClient.setApiKey(token);
        }
    }
}