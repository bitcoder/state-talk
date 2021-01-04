package com.automationintesting.e2databuilder.creators;

import com.automationintesting.e2databuilder.model.BookingRequestModel;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class BookingCreator {

    public static void createBooking(BookingRequestModel bookingPayload) {
        given()
            .contentType(ContentType.JSON)
            .body(bookingPayload)
            .post("http://localhost:8080/booking/");
    }
}
