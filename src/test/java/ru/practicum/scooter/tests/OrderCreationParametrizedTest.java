package ru.practicum.scooter.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized;
import ru.practicum.scooter.api.ApiConstants;
import ru.practicum.scooter.models.OrderData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Story("Order Creation")
public class OrderCreationParametrizedTest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = ApiConstants.BASE_URL;
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    @Description("Проверка: можно создать заказ без цвета")
    public void testCreateOrderWithoutColor() {
        OrderData order = new OrderData(
                "Иван",
                "Иванов",
                "Невский проспект, 10",
                1,
                "+71234567890",
                1,
                "2025-01-20",
                "Позвонить перед приездом",
                null
        );

        given()
                .contentType("application/json")
                .body(order)
                .post(ApiConstants.ORDER_CREATE)
                .then()
                .statusCode(ApiConstants.STATUS_CREATED)
                .body("track", notNullValue());
    }

    @Test
    @Description("Проверка: можно создать заказ с одним цветом BLACK")
    public void testCreateOrderWithBlackColor() {
        OrderData order = new OrderData(
                "Петр",
                "Петров",
                "Марсова поля, 25",
                2,
                "+71234567891",
                2,
                "2025-01-21",
                "Оставить у двери",
                Arrays.asList("BLACK")
        );

        given()
                .contentType("application/json")
                .body(order)
                .post(ApiConstants.ORDER_CREATE)
                .then()
                .statusCode(ApiConstants.STATUS_CREATED)
                .body("track", notNullValue());
    }

    @Test
    @Description("Проверка: можно создать заказ с одним цветом GREY")
    public void testCreateOrderWithGreyColor() {
        OrderData order = new OrderData(
                "Сергей",
                "Сергеев",
                "Садовая улица, 45",
                3,
                "+71234567892",
                3,
                "2025-01-22",
                "Позвонить",
                Arrays.asList("GREY")
        );

        given()
                .contentType("application/json")
                .body(order)
                .post(ApiConstants.ORDER_CREATE)
                .then()
                .statusCode(ApiConstants.STATUS_CREATED)
                .body("track", notNullValue());
    }

    @Test
    @Description("Проверка: можно создать заказ с обоими цветами")
    public void testCreateOrderWithBothColors() {
        OrderData order = new OrderData(
                "Анна",
                "Анненко",
                "Декабристов, 73",
                4,
                "+71234567893",
                1,
                "2025-01-23",
                "Без комментариев",
                Arrays.asList("BLACK", "GREY")
        );

        given()
                .contentType("application/json")
                .body(order)
                .post(ApiConstants.ORDER_CREATE)
                .then()
                .statusCode(ApiConstants.STATUS_CREATED)
                .body("track", notNullValue());
    }

    @Test
    @Description("Проверка: ответ содержит track при создании заказа")
    public void testResponseContainsTrack() {
        OrderData order = new OrderData(
                "Мария",
                "Маркина",
                "Рубинштейна, 100",
                5,
                "+71234567894",
                2,
                "2025-01-24",
                "Не беспокоить",
                null
        );

        given()
                .contentType("application/json")
                .body(order)
                .post(ApiConstants.ORDER_CREATE)
                .then()
                .statusCode(ApiConstants.STATUS_CREATED)
                .body("track", allOf(notNullValue(), greaterThan(0)));
    }

    @Test
    @Description("Проверка: ответ содержит корректный track")
    public void testTrackIsNumeric() {
        OrderData order = new OrderData(
                "Ольга",
                "Олегова",
                "Невский проспект, 50",
                1,
                "+71234567895",
                1,
                "2025-01-25",
                "Позвонить",
                Arrays.asList("BLACK")
        );

        given()
                .contentType("application/json")
                .body(order)
                .post(ApiConstants.ORDER_CREATE)
                .then()
                .statusCode(ApiConstants.STATUS_CREATED)
                .body("track", instanceOf(Integer.class));
    }
}
