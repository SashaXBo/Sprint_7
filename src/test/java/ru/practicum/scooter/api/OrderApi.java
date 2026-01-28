package ru.practicum.scooter.api;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import ru.practicum.scooter.models.OrderData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

public class OrderApi {
    
    private final RequestSpecification requestSpec;

    public OrderApi(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    // ========== CREATE ORDER ==========
    
    public ValidatableResponse createOrder(OrderData order) {
        return given()
                .spec(requestSpec)
                .body(order)
                .post(ApiConstants.ORDER_CREATE)
                .then();
    }

    public int createOrderAndGetTrack(OrderData order) {
        Response response = given()
                .spec(requestSpec)
                .body(order)
                .post(ApiConstants.ORDER_CREATE)
                .then()
                .statusCode(ApiConstants.STATUS_CREATED)
                .body("track", notNullValue())
                .extract()
                .response();
        
        return response.jsonPath().getInt("track");
    }

    public void assertOrderCreatedSuccessfully(OrderData order) {
        createOrder(order)
                .statusCode(ApiConstants.STATUS_CREATED)
                .body("track", greaterThan(0));
    }

    // ========== LIST ORDERS ==========
    
    public ValidatableResponse getOrdersList() {
        return given()
                .spec(requestSpec)
                .get(ApiConstants.ORDER_LIST)
                .then();
    }

    public void assertOrdersListNotEmpty() {
        getOrdersList()
                .statusCode(ApiConstants.STATUS_SUCCESS)
                .body("orders", notNullValue())
                .body("orders", hasSize(greaterThan(0)));
    }

    public void assertOrdersListExists() {
        getOrdersList()
                .statusCode(ApiConstants.STATUS_SUCCESS)
                .body("orders", notNullValue());
    }
}
