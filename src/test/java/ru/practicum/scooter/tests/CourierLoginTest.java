package ru.practicum.scooter.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import ru.practicum.scooter.api.CourierApi;
import ru.practicum.scooter.models.CourierData;
import ru.practicum.scooter.tests.base.BaseTest;
import ru.practicum.scooter.utils.TestDataGenerator;


@Story("Courier Login")
public class CourierLoginTest extends BaseTest {

    private CourierApi courierApi;
    private TestDataGenerator testData;
    private CourierData testCourier;

    @Before
    public void setup() {
        courierApi = new CourierApi(requestSpec);
        testData = new TestDataGenerator();
    }

    @After
    public void cleanup() {
        if (testCourier != null) {
            courierApi.deleteCreatedCourier(testCourier);
        }
    }

    @Test
    @Description("Курьер может авторизоваться с корректными данными")
    public void testCourierCanLogin() {
        CourierData courier = testData.createValidCourier();
        Response createResponse = courierApi.createCourier(courier).extract().response();
        Assert.assertEquals("Курьер должен быть успешно создан", 201, createResponse.statusCode());

        Response loginResponse = courierApi.loginCourier(courier).extract().response();
        Assert.assertEquals("Успешная авторизация", 200, loginResponse.statusCode());
        Assert.assertNotNull("Ответ должен содержать ID", loginResponse.jsonPath().getInt("id"));
    }

    @Test
    @Description("Для авторизации нужно передать все обязательные поля (без логина)")
    public void testLoginWithoutLogin() {
        CourierData courier = testData.createCourierWithoutLogin();
        Response response = courierApi.loginCourier(courier).extract().response();
        Assert.assertTrue("Должен быть статус 400 или 504", response.statusCode() == 400 || response.statusCode() == 504);
        Assert.assertNotNull("Должен быть сообщение об ошибке", response.jsonPath().getString("message"));
    }

    @Test
    @Description("Для авторизации нужно передать все обязательные поля (без пароля)")
    public void testLoginWithoutPassword() {
        testCourier = testData.createCourierWithoutPassword();
        Response response = courierApi.loginCourier(testCourier).extract().response();
        Assert.assertTrue("Должен быть статус 400 или 504", response.statusCode() == 400 || response.statusCode() == 504);
    }

    @Test
    @Description("Система вернёт ошибку, если неправильно указать логин")
    public void testLoginWithWrongLogin() {
        CourierData courier = testData.createValidCourier();
        courierApi.createCourier(courier).statusCode(201);

        CourierData wrongCourier = new CourierData(
                "wrong_login_" + System.currentTimeMillis(),
                courier.getPassword(),
                courier.getFirstName()
        );
        Response response = courierApi.loginCourier(wrongCourier).extract().response();
        Assert.assertTrue("Должен быть статус 404 или 401",
                response.statusCode() == 404 || response.statusCode() == 401);
    }

    @Test
    @Description("Система вернёт ошибку, если неправильно указать пароль")
    public void testLoginWithWrongPassword() {
        CourierData courier = testData.createValidCourier();
        courierApi.createCourier(courier).statusCode(201);


        CourierData wrongCourier = new CourierData(
                courier.getLogin(),
                "wrong_password_" + System.currentTimeMillis(),
                courier.getFirstName()
        );
        Response response = courierApi.loginCourier(wrongCourier).extract().response();
        Assert.assertEquals("Неверные учетные данные", 404, response.statusCode());
        Assert.assertNotNull("Должен быть сообщение об ошибке", response.jsonPath().getString("message"));
    }

    @Test
    @Description("Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    public void testLoginNonexistentUser() {
        CourierData nonexistent = new CourierData(
                testData.generateRandomLogin(),
                testData.generatePassword(),
                testData.generateCourierFirstName()
        );
        Response response = courierApi.loginCourier(nonexistent).extract().response();
        Assert.assertTrue("Должен быть статус 404 или 504", response.statusCode() == 404 || response.statusCode() == 504);
    }

    @Test
    @Description("Успешный запрос логина возвращает id")
    public void testSuccessfulLoginReturnsId() {
        CourierData courier = testData.createValidCourier();
        courierApi.createCourier(courier).statusCode(201);


        int courierId = courierApi.loginCourierAndGetId(courier);
        Assert.assertTrue("ID курьера должен быть больше 0", courierId > 0);
    }
}
