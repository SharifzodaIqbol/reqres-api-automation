package org.example.reqres.tests;

import io.qameta.allure.*;
import org.example.reqres.models.UserRequest;
import org.example.reqres.models.UserResponse;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LoginTest extends BaseTest {

    @Test
    public void testSuccessfulLogin() throws IOException {
        UserRequest request = new UserRequest("eve.holt@reqres.in", "cityslicka");
        Response<UserResponse> response = api.loginUser(request).execute();

        assertThat(response.code()).isEqualTo(200);
        assertThat(response.body()).isNotNull();
        assertThat(response.body().getToken()).isNotEmpty();
    }

    @Test
    public void testLoginWithoutPassword() throws IOException {
        UserRequest request = new UserRequest("peter@klaven", "");
        Response<UserResponse> response = api.loginUser(request).execute();

        assertThat(response.code()).isEqualTo(400);
    }
}