package com.tau.restassuredcourse;

import org.junit.jupiter.api.Test;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Chapter2Test {

    @Test
    public void requestUsZipCode90210_checkStatusCode_expectHttp200() {
        given().when().get("http://zippopotam.us/us/90210").then().assertThat().statusCode(200);
    }

    @Test
    public void requestUsZipCode90210_checkStatusCode_expectHttp201FAIL() {
        given().when().get("http://zippopotam.us/us/90210").then().assertThat().statusCode(201);
    }

    @Test
    public void requestUsZipCode90210_checkContentType_expectApplicationJson() {
        given().when().get("http://zippopotam.us/us/90210").then().assertThat().contentType(ContentType.JSON);

    }

    @Test
    public void requestUsZipCode90210_checkContentType_expectApplicationJsonFAIL() {
        given().when().get("http://zippopotam.us/us/90210").then().assertThat().contentType(ContentType.XML);

    }
}
