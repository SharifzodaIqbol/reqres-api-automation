package org.example.booker.tests.bookings;

import org.example.booker.tests.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateBookingTest extends BaseTest {

    @Test
    @DisplayName("Создание нового бронирования")
    void createValidBooking() {
        var response = createBookingAndGetId(CFG.bookingFirstname());

        assertThat(response.code()).isEqualTo(200);
        assertThat(response.body()).isNotNull();
        assertThat(response.body().getBookingid()).isNotNull();
        assertThat(response.body().getBooking().getFirstname())
                .isEqualTo(CFG.bookingFirstname());
    }

    @Test
    @DisplayName("Создание нового бронирования без имени бронировщика")
    void createInvalidBooking() {
        var response = createBookingAndGetId("");
        assertThat(response.code()).isEqualTo(200);
        assertThat(response.body().getBooking().getFirstname()).isBlank();
    }
}