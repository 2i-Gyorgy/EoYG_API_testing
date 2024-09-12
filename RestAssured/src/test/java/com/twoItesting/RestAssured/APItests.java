package com.twoItesting.RestAssured;

import io.github.cdimascio.dotenv.Dotenv;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.lessThan;

public class APItests {

    @BeforeAll
    static void setUpDefaultRequestSpec() {
        Dotenv dotenv = null;
        dotenv = Dotenv.configure().load();
        RequestSpecification spec = given();
        spec.baseUri("http://localhost");
        spec.port(2002);
        spec.basePath("/api");
        spec.contentType(ContentType.JSON);
        spec.auth().basic(dotenv.get("API_USER"), dotenv.get("API_PASSWORD"));

        requestSpecification = spec;
    }

    @Test
    void requstAllProducts_checkStatusCode_expecHttp200() {
        given().when().get("/products").then().assertThat().statusCode(200);
    }

    @Test
    void requstAllProducts_checkContentType_expectApplicaitonJson() {
        given().when().get("/products").then().assertThat().contentType(ContentType.JSON);
    }

    @Test
    void requestAllProducts_checkResponseTime_expectResponseTimeIsLessThan1000ms() {
        given().when().get("/products").then().time(lessThan(1000L));
    }

    @Test
    void requestProductWithID1_checkNameInResponseBody_expectIpad() { // get Product ID=1 and check that name is 'Ipad'
        System.out.println("Test getProductWithID1 response body:");
        given().when().get("/products/1").then().log().body().assertThat().body("name", equalTo("iPad"));
    }

    @Test
    void runComplexTest_checkComplexRequirements_expectItToAllGoToPlan() {

        System.out.println("Test newProduct:");

        // 1. create new product and check that status code is 201. Store product ID for later use

        // JSONObject is a class that represents a Simple JSON.
        // We can add Key - Value pairs using the put method
        JSONObject postRequestParams = new JSONObject();
        postRequestParams.put("name", "mouse");
        postRequestParams.put("price", "20");

        // Add a header stating the Request body is a JSON
        requestSpecification.header("Content-Type", "application/json"); // Add the Json to the body of the request
        requestSpecification.body(postRequestParams.toJSONString()); // Post the request and check the response

        int newProductID = when().post("/products")
                .then().log().body()
                .assertThat().statusCode(201)
                .extract()
                .path("id");
        System.out.println("new product ID is: " + newProductID);

        // 2. update the newly created product and check status code is 200

        // JSONObject is a class that represents a Simple JSON.
        // We can add Key - Value pairs using the put method
        JSONObject putRequestParams = new JSONObject();
        putRequestParams.put("name", "xbox");
        putRequestParams.put("price", "350");

        // Add a header stating the Request body is a JSON
        requestSpecification.header("Content-Type", "application/json"); // Add the Json to the body of the request
        requestSpecification.body(putRequestParams.toJSONString()); // Post the request and check the response

        when().put("/products/" + newProductID)
                .then().log().body()
                .assertThat().statusCode(200);

        // 3. delete newly created product
        when().delete("/products/" + newProductID)
                .then().log().body()
                .assertThat().statusCode(200);
    }
}