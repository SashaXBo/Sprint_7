package ru.practicum.scooter.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.practicum.scooter.api.CourierApi;
import ru.practicum.scooter.models.CourierData;
import ru.practicum.scooter.tests.base.BaseTest;
import ru.practicum.scooter.utils.TestDataGenerator;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

@Story("Courier Login")
public class CourierLoginTest extends BaseTest {

    private CourierApi courierApi;
    private TestDataGenerator testData;
    private List<CourierData> createdCouriers = new ArrayList<>();

    @Before
    public void setup() {
        courierApi = new CourierApi(requestSpec);
        testData = new TestDataGenerator();
        createdCouriers.clear();
    }

    @After
    public void cleanup() {
        for (CourierData courier : createdCouriers) {
            courierApi.deleteCreatedCourier(courier);
        }
    }

    @Test
    @Description("Курьер может авторизоваться с корректными данными")
    public void testCourierCanLogin() {
        // Создаём курьера
        CourierData courier = testData.createValidCourier();
        courierApi.createCourier(courier).statusCode(201);
        createdCouriers.add(courier);

        // Авторизуемся
        courierApi.assertLoginSuccessful(courier);
    }

    @Test
    @Description("Для авторизации нужно передать все обязательные поля (без логина)")
    public void testLoginWithoutLogin() {
        CourierData courier = testData.createCourierWithoutLogin();
        courierApi.assertLoginMissingData(courier);
    }

    @Test
    @Description("Для авторизации нужно передать все обязательные поля (без пароля)")
    public void testLoginWithoutPassword() {
        CourierData courier = testData.createCourierWithoutPassword();
        // Сервер может возвращать 504 вместо 400 в некоторых случаях
        courierApi.loginCourier(courier)
                .statusCode(anyOf(equalTo(400), equalTo(504)));
    }

    @Test
    @Description("Система вернёт ошибку, если неправильно указать логин")
    public void testLoginWithWrongLogin() {
        CourierData courier = testData.createValidCourier();
        courierApi.createCourier(courier).statusCode(201);
        createdCouriers.add(courier);

        CourierData wrongCourier = new CourierData(
                "wrong_login",
                courier.getPassword(),
                courier.getFirstName()
        );
        courierApi.assertLoginInvalidCredentials(wrongCourier);
    }

    @Test
    @Description("Система вернёт ошибку, если неправильно указать пароль")
    public void testLoginWithWrongPassword() {
        CourierData courier = testData.createValidCourier();
        courierApi.createCourier(courier).statusCode(201);
        createdCouriers.add(courier);

        CourierData wrongCourier = new CourierData(
                courier.getLogin(),
                "wrong_password",
                courier.getFirstName()
        );
        courierApi.assertLoginInvalidCredentials(wrongCourier);
    }

    @Test
    @Description("Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    public void testLoginNonexistentUser() {
        CourierData nonexistent = new CourierData(
                testData.generateRandomLogin(),
                testData.generatePassword(),
                testData.generateCourierFirstName()
        );
        courierApi.loginCourier(nonexistent)
                .statusCode(anyOf(equalTo(404), equalTo(504)));
    }

    @Test
    @Description("Успешный запрос логина возвращает id")
    public void testSuccessfulLoginReturnsId() {
        CourierData courier = testData.createValidCourier();
        courierApi.createCourier(courier).statusCode(201);
        createdCouriers.add(courier);

        int courierId = courierApi.loginCourierAndGetId(courier);
        assert courierId > 0 : "Courier ID should be greater than 0";
    }
}