package ru.practicum.scooter.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.junit.Before;
import org.junit.Test;
import ru.practicum.scooter.api.OrderApi;
import ru.practicum.scooter.models.OrderData;
import ru.practicum.scooter.tests.base.BaseTest;
import ru.practicum.scooter.utils.TestDataGenerator;

@Story("Order Creation")
public class OrderCreationTest extends BaseTest {

    private OrderApi orderApi;
    private TestDataGenerator testData;

    @Before
    public void setup() {
        orderApi = new OrderApi(requestSpec);
        testData = new TestDataGenerator();
    }

    @Test
    @Description("Заказ можно создать с чёрным цветом")
    public void testOrderCreationWithBlackColor() {
        OrderData order = testData.createOrderWithBlackColor();
        orderApi.assertOrderCreatedSuccessfully(order);
    }

    @Test
    @Description("Заказ можно создать с серым цветом")
    public void testOrderCreationWithGreyColor() {
        OrderData order = testData.createOrderWithGreyColor();
        orderApi.assertOrderCreatedSuccessfully(order);
    }

    @Test
    @Description("Заказ можно создать с обоими цветами")
    public void testOrderCreationWithBothColors() {
        OrderData order = testData.createOrderWithBothColors();
        orderApi.assertOrderCreatedSuccessfully(order);
    }

    @Test
    @Description("Заказ можно создать без цвета")
    public void testOrderCreationWithoutColor() {
        OrderData order = testData.createOrderWithoutColor();
        orderApi.assertOrderCreatedSuccessfully(order);
    }

    @Test
    @Description("Тело ответа при создании заказа содержит track")
    public void testOrderResponseContainsTrack() {
        OrderData order = testData.createOrderWithBlackColor();
        int track = orderApi.createOrderAndGetTrack(order);
        assert track > 0 : "Track should be greater than 0";
    }

    @Test
    @Description("Успешное создание заказа возвращает код 201")
    public void testOrderCreationReturnsStatusCode201() {
        OrderData order = testData.createOrderWithGreyColor();
        orderApi.createOrder(order).statusCode(201);
    }
}