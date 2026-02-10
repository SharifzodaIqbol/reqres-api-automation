package org.example.booker.tests;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;

public class BookingTest extends BaseTest {

    @Test
    public void testGetBookingIds() throws IOException {
        var response = api.getBookingIds().execute();

        assertThat(response.code()).isEqualTo(200);
        assertThat(response.body()).isNotNull();
    }
}