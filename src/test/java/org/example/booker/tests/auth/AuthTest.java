package org.example.booker.tests.auth;

import org.example.booker.model.AuthRequest;
import org.example.booker.model.AuthResponse;
import org.example.booker.tests.BaseTest;
import org.example.booker.tests.utils.ConfigReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthTest extends BaseTest {

    @Test
    @DisplayName("Успешное получение токена с валидными данными")
    void loginSuccess() {
        var response = login(
                ConfigReader.getProperty("auth.username"),
                ConfigReader.getProperty("auth.password")
        );

        assertThat(response.code()).isEqualTo(200);
        assertThat(response.body()).isNotNull();
        assertThat(response.body().getToken()).isNotBlank();
    }

    @Test
    @DisplayName("Ошибка авторизации при неверном пароле")
    void loginWithWrongPassword() {
        var response = login(ConfigReader.getProperty("auth.username"), "wrong_password");

        assertThat(response.code()).isEqualTo(200);
        assertThat(response.body().getToken()).isNull();
    }

    @Test
    @DisplayName("Пустой логин")
    void loginEmpty() {
        var response = login("", "");
        assertThat(response.code()).isEqualTo(200);
        assertThat(response.body().getToken()).isNull();
    }

    private Response<AuthResponse> login(String username, String password) {
        AuthRequest auth = new AuthRequest()
                .username(username)
                .password(password);
        return execute(authApi.createToken(auth));
    }
}