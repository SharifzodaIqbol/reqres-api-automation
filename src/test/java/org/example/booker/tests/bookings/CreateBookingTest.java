package org.example.booker.tests.bookings;

import org.example.booker.tests.BaseTest;
import org.example.booker.tests.utils.ConfigReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateBookingTest extends BaseTest {

    @Test
    @DisplayName("Создание нового бронирования")
    void createValidBooking() {
        var response = createBookingAndGetId(ConfigReader.getProperty("test.booking.firstname"));

        assertThat(response.code()).isEqualTo(200);
        assertThat(response.body()).isNotNull();
        assertThat(response.body().getBookingid()).isNotNull();
        assertThat(response.body().getBooking().getFirstname())
                .isEqualTo(ConfigReader.getProperty("test.booking.firstname"));
    }

    @Test
    @DisplayName("Создание нового бронирования без имени бронировщика")
    void createInvalidBooking() {
        var response = createBookingAndGetId("");
        assertThat(response.code()).isEqualTo(200);
        assertThat(response.body().getBooking().getFirstname()).isBlank();
    }
}