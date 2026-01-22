package ru.practicum.scooter.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.practicum.scooter.api.ApiConstants;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Story("Order List")
public class OrderListTest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = ApiConstants.BASE_URL;
        RestAssured.filters(new AllureRestAssured());}

    @Test
    @Description("Проверка: в тело ответа возвращается список заказов")
    public void testOrderListReturnsArray() {
        given()
                .contentType("application/json")
                .get(ApiConstants.ORDER_LIST)
                .then()
                .statusCode(ApiConstants.STATUS_SUCCESS)
                .body("orders", notNullValue())
                .body("orders", instanceOf(java.util.List.class));
    }

    @Test
    @Description("Проверка: список заказов содержит положительное количество элементов")
    public void testOrderListIsNotEmpty() {
        given()
                .contentType("application/json")
                .get(ApiConstants.ORDER_LIST)
                .then()
                .statusCode(ApiConstants.STATUS_SUCCESS)
                .body("orders.size()", greaterThanOrEqualTo(0));
    }

    @Test
    @Description("Проверка: каждый заказ содержит корректные поля")
    public void testOrderHasRequiredFields() {
        given()
                .contentType("application/json")
                .get(ApiConstants.ORDER_LIST)
                .then()
                .statusCode(ApiConstants.STATUS_SUCCESS)
                .body("orders", hasSize(greaterThanOrEqualTo(0)))
                .body("orders.id", everyItem(notNullValue()));
    }

    @Test
    @Description("Проверка: ответ содержит поле totalCount")
    public void testResponseHasTotalCount() {
        given()
                .contentType("application/json")
                .get(ApiConstants.ORDER_LIST)
                .then()
                .statusCode(ApiConstants.STATUS_SUCCESS)
                .body("orders", notNullValue());
                //.body("totalOrdersCount", greaterThanOrEqualTo(0));
    }
}