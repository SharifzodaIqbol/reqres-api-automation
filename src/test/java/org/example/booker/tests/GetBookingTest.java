package org.example.booker.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingTest extends BaseTest{
    @Test
    @DisplayName("Проверка получения существующего бронирования по ID")
    void getBooking() throws IOException {
        var response = api.getBooking(1).execute();

        assertThat(response.code()).isEqualTo(200);
        assertThat(response.body().getFirstname()).isEqualTo("Mary");
        assertThat(response.body().getLastname()).isEqualTo("Smith");
        assertThat(response.body().getBookingdates().getCheckin())
                .isEqualTo(LocalDate.of(2015, 8, 27));
    }
}
