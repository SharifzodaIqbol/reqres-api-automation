package org.example.booker.tests.bookings;

import org.example.booker.tests.BaseTest;
import org.example.booker.tests.utils.ConfigReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteBookingTest extends BaseTest {

    private String fullCookie;

    @BeforeAll
    void getTokenAuth() {
        fullCookie = "token=" + getAuthToken();
    }

    @Test
    @DisplayName("Удаление существующего бронирования")
    void deleteBookingTest() {
        var createResponse = createBookingAndGetId(ConfigReader.getProperty("test.booking.firstname"));
        Integer id = createResponse.body().getBookingid();

        var deleteResponse = execute(bookingApi.deleteBooking(id, fullCookie));
        assertThat(deleteResponse.code()).isEqualTo(201);

        var getResponse = execute(bookingApi.getBooking(id));
        assertThat(getResponse.code()).isEqualTo(404);
    }

    @Test
    @DisplayName("Попытка удаления без токена")
    void deleteWithoutTokenTest() {
        var createResponse = createBookingAndGetId(ConfigReader.getProperty("test.booking.firstname"));
        Integer id = createResponse.body().getBookingid();

        var deleteResponse = execute(bookingApi.deleteBooking(id, ""));
        assertThat(deleteResponse.code()).isEqualTo(403);
    }
}