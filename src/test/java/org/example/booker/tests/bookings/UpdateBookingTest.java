package org.example.booker.tests.bookings;

import org.example.booker.model.Booking;
import org.example.booker.model.BookingDates;
import org.example.booker.tests.BaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateBookingTest extends BaseTest {
    private String fullCookie;

    @BeforeAll
    void getTokenAuth() {
        fullCookie = "token=" + getAuthToken();
    }

    @Test
    @DisplayName("Полное обновление")
    void fullUpdate() {

        LocalDate checkinDate = LocalDate.now();
        LocalDate checkoutDate = LocalDate.now().plusDays(3);

        var createResponse = createBookingAndGetId(CFG.bookingFirstname());

        Integer id = createResponse.body().getBookingid();
        Booking updateData = createResponse.body().getBooking();

        updateData.setFirstname("Aleks");
        updateData.setLastname("Updated");
        updateData.setDepositpaid(false);
        updateData.setTotalprice(200);
        updateData.setBookingdates(new BookingDates()
                .checkin(checkinDate)
                .checkout(checkoutDate));
        updateData.setAdditionalneeds("Breakfast");

        var response = execute(bookingApi.updateBooking(id, fullCookie, updateData));

        assertThat(response.code()).isEqualTo(200);
        assertThat(response.body().getFirstname()).isEqualTo("Aleks");
        assertThat(response.body().getLastname()).isEqualTo("Updated");
        assertThat(response.body().getDepositpaid()).isFalse();
        assertThat(response.body().getTotalprice()).isEqualTo(200);
        assertThat(response.body().getAdditionalneeds()).isEqualTo("Breakfast");
        assertThat(response.body().getBookingdates().getCheckin()).isEqualTo(checkinDate);
    }

    @Test
    @DisplayName("Частичное обновление")
    void partialUpdate() {
        var createResponse = createBookingAndGetId(CFG.bookingFirstname());
        Integer id = createResponse.body().getBookingid();

        Booking patchData = createResponse.body().getBooking();
        patchData.setFirstname("Aleks");

        var response = execute(bookingApi.updateBooking(id, fullCookie, patchData));

        assertThat(response.code()).isEqualTo(200);
        assertThat(response.body().getFirstname()).isEqualTo("Aleks");
    }
}