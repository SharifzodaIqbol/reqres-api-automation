package org.example.booker.tests.utils;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"classpath:message.properties"})
public interface AppConfig extends Config {

    @Key("base.url")
    String baseUrl();

    @Key("content.type")
    String contentType();

    @Key("accept.header")
    String acceptHeader();

    @Key("auth.username")
    String username();

    @Key("auth.password")
    String password();

    @Key("test.booking.firstname")
    String bookingFirstname();

    @Key("test.booking.lastname")
    String bookingLastname();

    @Key("test.booking.price")
    int bookingPrice();

    @Key("test.booking.additional")
    String bookingAdditional();

    @Key("error.network")
    String errorNetwork();

    @Key("error.auth.fail")
    String errorAuthFail();

    @Key("error.auth.token_empty")
    String errorAuthTokenEmpty();

    @Key("error.server")
    String errorServer();
}