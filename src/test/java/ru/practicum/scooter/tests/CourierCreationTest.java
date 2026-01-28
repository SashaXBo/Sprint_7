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

@Story("Courier Creation")
public class CourierCreationTest extends BaseTest {

    private CourierApi courierApi;
    private TestDataGenerator testData;
    private CourierData testCourier; // **ОДИН курьер на тест**

    @Before
    public void setup() {
        courierApi = new CourierApi(requestSpec);
        testData = new TestDataGenerator();
    }

    @After
    public void cleanup() {
        // **Удаляем курьера только если он был создан**
        if (testCourier != null) {
            courierApi.deleteCreatedCourier(testCourier);
        }
    }

    @Test
    @Description("Успешное создание курьера с корректными данными")
    public void testCreateCourierWithValidData() {
        testCourier = testData.createValidCourier(); // **Сохраняем для cleanup**
        Response response = courierApi.createCourier(testCourier).extract().response();

        Assert.assertEquals("Курьер должен быть успешно создан", 201, response.statusCode());
        Assert.assertTrue("Ответ должен содержать ok: true", response.jsonPath().getBoolean("ok"));
    }

    @Test
    @Description("Невозможно создать двух курьеров с одинаковым логином")
    public void testCannotCreateDuplicateCourier() {
        testCourier = testData.createValidCourier(); // **Сохраняем для cleanup**

        // Первый курьер создаётся успешно
        Response firstResponse = courierApi.createCourier(testCourier).extract().response();
        Assert.assertEquals("Первый курьер создан успешно", 201, firstResponse.statusCode());

        // Второй с тем же логином — конфликт
        Response secondResponse = courierApi.createCourier(testCourier).extract().response();
        Assert.assertEquals("Дублирующийся логин", 409, secondResponse.statusCode());
        Assert.assertTrue("Сообщение о дубликате",
                secondResponse.jsonPath().getString("message").contains("логин уже используется"));
    }

    @Test
    @Description("Создание курьера без логина должно быть отклонено")
    public void testCreateCourierWithoutLogin() {
        testCourier = testData.createCourierWithoutLogin();
        Response response = courierApi.createCourier(testCourier).extract().response();

        Assert.assertEquals("Недостаточно данных", 400, response.statusCode());
        Assert.assertNotNull("Сообщение об ошибке", response.jsonPath().getString("message"));
    }

    @Test
    @Description("Создание курьера без пароля должно быть отклонено")
    public void testCreateCourierWithoutPassword() {
        testCourier = testData.createCourierWithoutPassword();
        Response response = courierApi.createCourier(testCourier).extract().response();

        Assert.assertEquals("Недостаточно данных", 400, response.statusCode());
        Assert.assertNotNull("Сообщение об ошибке", response.jsonPath().getString("message"));
    }

    @Test
    @Description("Создание курьера без имени — имя может быть опциональным полем")
    public void testCreateCourierWithoutFirstName() {
        testCourier = testData.createCourierWithoutFirstName(); // **Сохраняем для cleanup**
        Response response = courierApi.createCourier(testCourier).extract().response();

        Assert.assertEquals("Курьер создан успешно (имя опциональное)", 201, response.statusCode());
        Assert.assertTrue("Ответ должен содержать ok: true", response.jsonPath().getBoolean("ok"));
    }

    @Test
    @Description("Успешный запрос возвращает ok: true")
    public void testSuccessfulCreationReturnsOkTrue() {
        testCourier = testData.createValidCourier(); // **Сохраняем для cleanup**
        Response response = courierApi.createCourier(testCourier).extract().response();

        Assert.assertEquals("Статус создан", 201, response.statusCode());
        Assert.assertTrue("ok должно быть true", response.jsonPath().getBoolean("ok"));
    }

    @Test
    @Description("Создание с уже существующим логином возвращает конфликт")
    public void testDuplicateLoginReturnsConflict() {
        testCourier = testData.createValidCourier(); // **Сохраняем для cleanup**

        courierApi.createCourier(testCourier).statusCode(201);
        Response response = courierApi.createCourier(testCourier).extract().response();
        Assert.assertEquals("Конфликт при дубликате", 409, response.statusCode());
    }
}
