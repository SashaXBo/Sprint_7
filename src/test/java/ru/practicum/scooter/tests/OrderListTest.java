package ru.practicum.scooter.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.junit.Before;
import org.junit.Test;
import ru.practicum.scooter.api.OrderApi;
import ru.practicum.scooter.tests.base.BaseTest;

import static org.hamcrest.Matchers.notNullValue;

@Story("Order List")
public class OrderListTest extends BaseTest {

    private OrderApi orderApi;

    @Before
    public void setup() {
        orderApi = new OrderApi(requestSpec);
    }

    @Test
    @Description("В тело ответа возвращается список заказов")
    public void testOrdersListExists() {
        orderApi.assertOrdersListExists();
    }

    @Test
    @Description("Список заказов имеет структуру с полями orders")
    public void testOrdersListHasOrders() {
        orderApi.getOrdersList()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}