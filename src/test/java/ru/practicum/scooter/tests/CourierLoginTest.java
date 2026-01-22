package ru.practicum.scooter.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.practicum.scooter.api.ApiConstants;
import ru.practicum.scooter.models.CourierData;
import ru.practicum.scooter.utils.TestDataGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Story("Courier Login")
public class CourierLoginTest {

    private CourierData testCourier;

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = ApiConstants.BASE_URL;
        RestAssured.filters(new AllureRestAssured());}

    @Before
    public void createTestCourier() {
        // Создаем курьера для тестов авторизации
        testCourier = new CourierData(
                TestDataGenerator.generateRandomLogin(),
                TestDataGenerator.generatePassword(),
                TestDataGenerator.generateFirstName()
        );

        given()
                .contentType("application/json")
                .body(testCourier)
                .post(ApiConstants.COURIER_CREATE)
                .then()
                .statusCode(anyOf(equalTo(ApiConstants.STATUS_CREATED), equalTo(ApiConstants.STATUS_SUCCESS)));
    }

    @Test
    @Description("Проверка: курьер может авторизоваться")
    public void testCourierCanLogin() {
        CourierData loginData = new CourierData(testCourier.getLogin(), testCourier.getPassword(), null);

        given()
                .contentType("application/json")
                .body(loginData)
                .post(ApiConstants.COURIER_LOGIN)
                .then()
                .statusCode(ApiConstants.STATUS_SUCCESS)
                .body("id", notNullValue());
    }

    @Test
    @Description("Проверка: для авторизации нужны все обязательные поля")
    public void testMissingLoginField() {
        CourierData loginData = new CourierData(null, testCourier.getPassword(), null);

        given()
                .contentType("application/json")
                .body(loginData)
                .post(ApiConstants.COURIER_LOGIN)
                .then()
                .statusCode(ApiConstants.STATUS_BAD_REQUEST)
                .body("message", containsString("Недостаточно данных для входа"));
    }

    @Test
    @Description("Проверка: отсутствие пароля вызывает ошибку")
    public void testMissingPasswordField() {
        CourierData loginData = new CourierData(testCourier.getLogin(), null, null);

        given()
                .contentType("application/json")
                .body(loginData)
                .post(ApiConstants.COURIER_LOGIN)
                .then()
                .statusCode(ApiConstants.STATUS_BAD_REQUEST)
                .body("message", containsString("Недостаточно данных для входа"));
    }

    @Test
    @Description("Проверка: ошибка при неправильном логине")
    public void testIncorrectLogin() {
        CourierData loginData = new CourierData("wronglogin", testCourier.getPassword(), null);

        given()
                .contentType("application/json")
                .body(loginData)
                .post(ApiConstants.COURIER_LOGIN)
                .then()
                .statusCode(ApiConstants.STATUS_UNAUTHORIZED)
                .body("message", containsString("Учетная запись не найдена"));
    }

    @Test
    @Description("Проверка: ошибка при неправильном пароле")
    public void testIncorrectPassword() {
        CourierData loginData = new CourierData(testCourier.getLogin(), "wrongpassword", null);

        given()
                .contentType("application/json")
                .body(loginData)
                .post(ApiConstants.COURIER_LOGIN)
                .then()
                .statusCode(ApiConstants.STATUS_UNAUTHORIZED)
                .body("message", containsString("Учетная запись не найдена"));
    }

    @Test
    @Description("Проверка: авторизация с несуществующим пользователем")
    public void testLoginWithNonExistentUser() {
        CourierData loginData = new CourierData(
                TestDataGenerator.generateRandomLogin(),
                TestDataGenerator.generatePassword(),
                null
        );

        given()
                .contentType("application/json")
                .body(loginData)
                .post(ApiConstants.COURIER_LOGIN)
                .then()
                .statusCode(ApiConstants.STATUS_UNAUTHORIZED)
                .body("message", notNullValue());
    }

    @Test
    @Description("Проверка: успешная авторизация возвращает id")
    public void testSuccessfulLoginReturnsId() {
        CourierData loginData = new CourierData(testCourier.getLogin(), testCourier.getPassword(), null);

        given()
                .contentType("application/json")
                .body(loginData)
                .post(ApiConstants.COURIER_LOGIN)
                .then()
                .statusCode(ApiConstants.STATUS_SUCCESS)
                .body("id", allOf(notNullValue(), greaterThan(0)));
    }
}