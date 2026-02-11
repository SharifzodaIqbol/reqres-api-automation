package org.example.booker.tests;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.example.booker.api.AuthenticationApi;
import org.example.booker.api.BookingApi;
import org.example.booker.model.AuthRequest;
import org.example.booker.model.Booking;
import org.example.booker.model.BookingDates;
import org.example.booker.model.BookingResponse;
import org.example.booker.tests.utils.ConfigReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {

    protected BookingApi bookingApi;
    protected AuthenticationApi authApi;

    @BeforeAll
    void setup() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .header("Content-Type", ConfigReader.getProperty("content.type"))
                            .header("Accept", ConfigReader.getProperty("accept.header"))
                            .build();
                    return chain.proceed(request);
                })
                .build();

        Gson gson = Converters.registerAll(new GsonBuilder())
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConfigReader.getProperty("base.url"))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        bookingApi = retrofit.create(BookingApi.class);
        authApi = retrofit.create(AuthenticationApi.class);
    }

    protected <T> Response<T> execute(Call<T> call) {
        try {
            Response<T> response = call.execute();
            if (response.code() >= 500) {
                throw new AssertionError(ConfigReader.getMessage("error.server",
                        response.code(), response.errorBody().string()));
            }
            return response;
        } catch (IOException e) {
            throw new AssertionError(ConfigReader.getMessage("error.network") + e.getMessage(), e);
        }
    }

    protected Response<BookingResponse> createBookingAndGetId(String firstname) {
        Booking body = new Booking()
                .firstname(firstname)
                .lastname(ConfigReader.getProperty("test.booking.lastname"))
                .totalprice(Integer.valueOf(ConfigReader.getProperty("test.booking.price")))
                .depositpaid(true)
                .bookingdates(new BookingDates().checkin(LocalDate.now()).checkout(LocalDate.now().plusDays(7)))
                .additionalneeds(ConfigReader.getProperty("test.booking.additional"));

        return execute(bookingApi.createBooking(body));
    }

    protected String getAuthToken() {
        AuthRequest auth = new AuthRequest()
                .username(ConfigReader.getProperty("auth.username"))
                .password(ConfigReader.getProperty("auth.password"));

        var response = execute(authApi.createToken(auth));

        assertThat(response.isSuccessful())
                .withFailMessage(ConfigReader.getMessage("error.auth.fail", response.code()))
                .isTrue();

        String token = response.body().getToken();
        assertThat(token)
                .withFailMessage(ConfigReader.getMessage("error.auth.token_empty"))
                .isNotBlank();

        return token;
    }
}