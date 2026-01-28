package ru.practicum.scooter.api;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import ru.practicum.scooter.models.CourierData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierApi {

    private static final String ERROR_DUPLICATE_LOGIN = "Этот логин уже используется";
    private static final String ERROR_MISSING_DATA = "Недостаточно данных для создания учетной записи";

    private final RequestSpecification requestSpec;

    public CourierApi(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    // ========== CREATE COURIER ==========

    public ValidatableResponse createCourier(CourierData courier) {
        return given()
                .spec(requestSpec)
                .body(courier)
                .post(ApiConstants.COURIER_CREATE)
                .then();
    }

    public void assertCourierCreatedSuccessfully(CourierData courier) {
        createCourier(courier)
                .statusCode(ApiConstants.STATUS_CREATED)
                .body("ok", equalTo(true));
    }

    public void assertDuplicateLoginError(CourierData courier) {
        createCourier(courier)
                .statusCode(ApiConstants.STATUS_CONFLICT)
                .body("message", containsString(ERROR_DUPLICATE_LOGIN));
    }

    public void assertBadRequestError(CourierData courier) {
        createCourier(courier)
                .statusCode(ApiConstants.STATUS_BAD_REQUEST)
                .body("message", notNullValue());
    }

    // ========== LOGIN COURIER ==========

    public ValidatableResponse loginCourier(CourierData courier) {
        return given()
                .spec(requestSpec)
                .body(courier)
                .post(ApiConstants.COURIER_LOGIN)
                .then();
    }

    public int loginCourierAndGetId(CourierData courier) {
        Response response = given()
                .spec(requestSpec)
                .body(courier)
                .post(ApiConstants.COURIER_LOGIN)
                .then()
                .extract()
                .response();

        return response.jsonPath().getInt("id");
    }

    public void assertLoginSuccessful(CourierData courier) {
        loginCourier(courier)
                .statusCode(ApiConstants.STATUS_SUCCESS)
                .body("id", notNullValue());
    }

    public void assertLoginInvalidCredentials(CourierData courier) {
        loginCourier(courier)
                .statusCode(ApiConstants.STATUS_UNAUTHORIZED)
                .body("message", notNullValue());
    }

    public void assertLoginMissingData(CourierData courier) {
        loginCourier(courier)
                .statusCode(ApiConstants.STATUS_BAD_REQUEST)
                .body("message", notNullValue());
    }

    public void assertLoginNotFound(CourierData courier) {
        loginCourier(courier)
                .statusCode(ApiConstants.STATUS_NOT_FOUND)
                .body("message", notNullValue());
    }

    // ========== DELETE COURIER ==========

    public void deleteCourier(int courierId) {
        given()
                .spec(requestSpec)
                .pathParam("id", courierId)
                .delete(ApiConstants.COURIER_DELETE)
                .then()
                .statusCode(ApiConstants.STATUS_SUCCESS);
    }

    public void deleteCreatedCourier(CourierData courier) {
        try {
            int courierId = loginCourierAndGetId(courier);
            deleteCourier(courierId);
        } catch (Exception e) {
            System.err.println("Failed to delete courier with login " + courier.getLogin() + ": " + e.getMessage());
        }
    }
}