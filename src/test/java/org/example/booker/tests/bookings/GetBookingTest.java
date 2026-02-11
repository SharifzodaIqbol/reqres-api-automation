package org.example.booker.tests.bookings;

import org.example.booker.tests.BaseTest;
import org.example.booker.tests.utils.ConfigReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingTest extends BaseTest {

    @Test
    @DisplayName("Проверка получения существующего бронирования")
    void getBooking() {
        var response = createBookingAndGetId(ConfigReader.getProperty("test.booking.firstname"));
        assertThat(response.code()).isEqualTo(200);
        assertThat(response.body()).isNotNull();
        assertThat(response.body().getBooking().getFirstname())
                .isEqualTo(ConfigReader.getProperty("test.booking.firstname"));
    }

    @Test
    @DisplayName("Получение списка ID")
    void getBookingIds() {
        var response = execute(bookingApi.getBookingIds());
        assertThat(response.code()).isEqualTo(200);
        assertThat(response.isSuccessful()).isTrue();
    }

    @Test
    @DisplayName("Получение не существующего ID")
    void getInvalidBookingId() {
        var response = execute(bookingApi.getBooking(-1));
        assertThat(response.code()).isEqualTo(404);
        assertThat(response.body()).isNull();
    }
}