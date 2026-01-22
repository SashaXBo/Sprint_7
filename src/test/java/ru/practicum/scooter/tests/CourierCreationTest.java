package ru.practicum.scooter.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.practicum.scooter.api.ApiConstants;
import ru.practicum.scooter.models.CourierData;
import ru.practicum.scooter.utils.TestDataGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Story("Courier Creation")
public class CourierCreationTest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = ApiConstants.BASE_URL;
        RestAssured.filters(new AllureRestAssured());}

    @Test
    @Description("Проверка: курьера можно создать")
    public void testCourierCanBeCreated() {
        CourierData courier = new CourierData(
                TestDataGenerator.generateRandomLogin(),
                TestDataGenerator.generatePassword(),
                TestDataGenerator.generateFirstName()
        );

        given()
                .contentType("application/json")
                .body(courier)
                .post(ApiConstants.COURIER_CREATE)
                .then()
                .statusCode(ApiConstants.STATUS_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @Description("Проверка: нельзя создать двух одинаковых курьеров")
    public void testCannotCreateDuplicateCourier() {
        String login = TestDataGenerator.generateRandomLogin();
        String password = TestDataGenerator.generatePassword();
        String firstName = TestDataGenerator.generateFirstName();

        CourierData courier = new CourierData(login, password, firstName);

        // Создаем первого курьера
        given()
                .contentType("application/json")
                .body(courier)
                .post(ApiConstants.COURIER_CREATE)
                .then()
                .statusCode(ApiConstants.STATUS_CREATED);

        // Пытаемся создать второго с тем же логином
        given()
                .contentType("application/json")
                .body(courier)
                .post(ApiConstants.COURIER_CREATE)
                .then()
                .statusCode(ApiConstants.STATUS_CONFLICT)
                .body("message", containsString("Этот логин уже используется"));
    }

    @Test
    @Description("Проверка: обязательные поля для создания курьера")
    public void testMissingLoginField() {
        CourierData courier = new CourierData(
                null,  // без логина
                TestDataGenerator.generatePassword(),
                TestDataGenerator.generateFirstName()
        );

        given()
                .contentType("application/json")
                .body(courier)
                .post(ApiConstants.COURIER_CREATE)
                .then()
                .statusCode(ApiConstants.STATUS_BAD_REQUEST)
                .body("message", containsString("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @Description("Проверка: обязательное поле пароль")
    public void testMissingPasswordField() {
        CourierData courier = new CourierData(
                TestDataGenerator.generateRandomLogin(),
                null,  // без пароля
                TestDataGenerator.generateFirstName()
        );

        given()
                .contentType("application/json")
                .body(courier)
                .post(ApiConstants.COURIER_CREATE)
                .then()
                .statusCode(ApiConstants.STATUS_BAD_REQUEST)
                .body("message", containsString("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @Description("Проверка: успешный запрос возвращает ok: true")
    public void testSuccessfulResponseReturnsOkTrue() {
        CourierData courier = new CourierData(
                TestDataGenerator.generateRandomLogin(),
                TestDataGenerator.generatePassword(),
                TestDataGenerator.generateFirstName()
        );

        given()
                .contentType("application/json")
                .body(courier)
                .post(ApiConstants.COURIER_CREATE)
                .then()
                .statusCode(ApiConstants.STATUS_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @Description("Проверка: создание с логином, который уже существует")
    public void testDuplicateLoginError() {
        String login = TestDataGenerator.generateRandomLogin();
        String password = TestDataGenerator.generatePassword();

        CourierData courier1 = new CourierData(login, password, TestDataGenerator.generateFirstName());

        // Создаем первого курьера
        given()
                .contentType("application/json")
                .body(courier1)
                .post(ApiConstants.COURIER_CREATE)
                .then()
                .statusCode(ApiConstants.STATUS_CREATED);

        CourierData courier2 = new CourierData(login, TestDataGenerator.generatePassword(), TestDataGenerator.generateFirstName());

        // Пытаемся создать второго с существующим логином
        given()
                .contentType("application/json")
                .body(courier2)
                .post(ApiConstants.COURIER_CREATE)
                .then()
                .statusCode(ApiConstants.STATUS_CONFLICT)
                .body("message", notNullValue());
    }

    @Test
    @Description("Проверка: возвращается правильный код ответа при успехе")
    public void testCorrectStatusCodeOnSuccess() {
        CourierData courier = new CourierData(
                TestDataGenerator.generateRandomLogin(),
                TestDataGenerator.generatePassword(),
                TestDataGenerator.generateFirstName()
        );

        given()
                .contentType("application/json")
                .body(courier)
                .post(ApiConstants.COURIER_CREATE)
                .then()
                .statusCode(anyOf(equalTo(ApiConstants.STATUS_CREATED), equalTo(ApiConstants.STATUS_SUCCESS)));
    }
}