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

@Story("Courier Creation")
public class CourierCreationTest extends BaseTest {

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
    @Description("Успешное создание курьера с корректными данными")
    public void testCreateCourierWithValidData() {
        CourierData courier = testData.createValidCourier();
        courierApi.assertCourierCreatedSuccessfully(courier);
        createdCouriers.add(courier);
    }

    @Test
    @Description("Невозможно создать двух курьеров с одинаковым логином")
    public void testCannotCreateDuplicateCourier() {
        CourierData courier = testData.createValidCourier();
        courierApi.createCourier(courier).statusCode(201);
        createdCouriers.add(courier);

        courierApi.assertDuplicateLoginError(courier);
    }

    @Test
    @Description("Создание курьера без логина должно быть отклонено")
    public void testCreateCourierWithoutLogin() {
        CourierData courier = testData.createCourierWithoutLogin();
        courierApi.assertBadRequestError(courier);
    }

    @Test
    @Description("Создание курьера без пароля должно быть отклонено")
    public void testCreateCourierWithoutPassword() {
        CourierData courier = testData.createCourierWithoutPassword();
        courierApi.assertBadRequestError(courier);
    }

    @Test
    @Description("Создание курьера без имени — имя может быть опциональным полем")
    public void testCreateCourierWithoutFirstName() {
        CourierData courier = testData.createCourierWithoutFirstName();
        // firstName может быть опциональным, поэтому тест проверяет успешное создание
        courierApi.createCourier(courier)
                .statusCode(201)
                .body("ok", org.hamcrest.Matchers.equalTo(true));
        createdCouriers.add(courier);
    }

    @Test
    @Description("Успешный запрос возвращает ok: true")
    public void testSuccessfulCreationReturnsOkTrue() {
        CourierData courier = testData.createValidCourier();
        courierApi.createCourier(courier)
                .statusCode(201)
                .body("ok", org.hamcrest.Matchers.equalTo(true));
        createdCouriers.add(courier);
    }

    @Test
    @Description("Создание с уже существующим логином возвращает конфликт")
    public void testDuplicateLoginReturnsConflict() {
        CourierData courier = testData.createValidCourier();
        courierApi.createCourier(courier).statusCode(201);
        createdCouriers.add(courier);

        courierApi.createCourier(courier).statusCode(409);
    }
}