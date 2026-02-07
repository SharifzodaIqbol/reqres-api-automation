package org.example.reqres.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserListTest extends BaseTest{

    @Test
    public void testGetUsersList() throws IOException {
        var response = api.getUsers(2).execute();

        assertThat(response.code()).isEqualTo(200);
        assertThat(response.body()).isNotNull();
    }
}