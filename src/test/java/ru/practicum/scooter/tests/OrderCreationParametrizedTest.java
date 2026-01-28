package ru.practicum.scooter.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.practicum.scooter.api.OrderApi;
import ru.practicum.scooter.models.OrderData;
import ru.practicum.scooter.tests.base.BaseTest;
import ru.practicum.scooter.utils.TestDataGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Story("Order Creation Parameterized")
@RunWith(Parameterized.class)
public class OrderCreationParametrizedTest extends BaseTest {

    private OrderApi orderApi;
    private TestDataGenerator testData;
    private OrderData order;
    private String testDescription;

    public OrderCreationParametrizedTest(OrderData order, String testDescription) {
        this.order = order;
        this.testDescription = testDescription;
    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> data() {
        TestDataGenerator testData = new TestDataGenerator();
        return Arrays.asList(new Object[][] {
                { createOrderWithBlackColor(testData), "Заказ с чёрным цветом" },
                { createOrderWithGreyColor(testData), "Заказ с серым цветом" },
                { createOrderWithBothColors(testData), "Заказ с обоими цветами" },
                { createOrderWithoutColor(testData), "Заказ без цвета" }
        });
    }

    private static OrderData createOrderWithBlackColor(TestDataGenerator testData) {
        OrderData order = new OrderData();
        order.setColor(Arrays.asList("black"));
        order.setFirstName(testData.generateFirstName());
        order.setLastName(testData.generateLastName());
        order.setAddress(testData.generateAddress());
        order.setMetroStation(testData.generateMetroStation());
        order.setPhone(testData.generatePhone());
        order.setRentTime(5);
        order.setDeliveryDate(testData.generateDeliveryDate());
        order.setComment("Test order with BLACK color");
        return order;
    }

    private static OrderData createOrderWithGreyColor(TestDataGenerator testData) {
        OrderData order = new OrderData();
        order.setColor(Arrays.asList("grey"));
        order.setFirstName(testData.generateFirstName());
        order.setLastName(testData.generateLastName());
        order.setAddress(testData.generateAddress());
        order.setMetroStation(testData.generateMetroStation());
        order.setPhone(testData.generatePhone());
        order.setRentTime(5);
        order.setDeliveryDate(testData.generateDeliveryDate());
        order.setComment("Test order with GREY color");
        return order;
    }

    private static OrderData createOrderWithBothColors(TestDataGenerator testData) {
        OrderData order = new OrderData();
        order.setColor(Arrays.asList("black", "grey"));
        order.setFirstName(testData.generateFirstName());
        order.setLastName(testData.generateLastName());
        order.setAddress(testData.generateAddress());
        order.setMetroStation(testData.generateMetroStation());
        order.setPhone(testData.generatePhone());
        order.setRentTime(5);
        order.setDeliveryDate(testData.generateDeliveryDate());
        order.setComment("Test order with BOTH colors");
        return order;
    }

    private static OrderData createOrderWithoutColor(TestDataGenerator testData) {
        OrderData order = new OrderData();
        order.setColor(new ArrayList<>()); // Empty color list
        order.setFirstName(testData.generateFirstName());
        order.setLastName(testData.generateLastName());
        order.setAddress(testData.generateAddress());
        order.setMetroStation(testData.generateMetroStation());
        order.setPhone(testData.generatePhone());
        order.setRentTime(5);
        order.setDeliveryDate(testData.generateDeliveryDate());
        order.setComment("Test order WITHOUT color");
        return order;
    }

    @Before
    public void setup() {
        orderApi = new OrderApi(requestSpec);
    }

    @Test
    @Description("Создание заказа успешно возвращает track")
    public void testOrderCreationReturnsTrack() {
        orderApi.assertOrderCreatedSuccessfully(order);
    }

    @Test
    @Description("Тело ответа при создании заказа содержит track")
    public void testOrderResponseContainsTrack() {
        int track = orderApi.createOrderAndGetTrack(order);
        assert track > 0 : "Track should be greater than 0";
    }
}