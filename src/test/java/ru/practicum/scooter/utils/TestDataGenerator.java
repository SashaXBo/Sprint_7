package ru.practicum.scooter.utils;

import com.github.javafaker.Faker;
import ru.practicum.scooter.models.CourierData;
import ru.practicum.scooter.models.OrderData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class TestDataGenerator {

    private final Faker faker = new Faker();


    public String generateRandomLogin() {
        return "courier_" + faker.name().firstName().toLowerCase() + "_" + faker.number().numberBetween(1000, 9999);
    }

    public String generatePassword() {
        return "Pass" + faker.number().numberBetween(1000, 9999) + "!" + System.currentTimeMillis();
    }

    public String generateCourierFirstName() {
        return faker.name().firstName();
    }

    public String generateFirstName() {
        return faker.name().firstName();
    }

    public String generateLastName() {
        return faker.name().lastName();
    }

    public String generatePhone() {
        return "+7" + faker.number().numberBetween(9000000000L, 9999999999L);
    }

    public String generateAddress() {
        return faker.address().streetAddress();
    }

    public String generateMetroStation() {
        String[] stations = {
                "Красная площадь",
                "Маяковская",
                "Кольцевая",
                "Арбатская",
                "Библиотека имени Ленина",
                "Боровицкая",
                "Охотный ряд"
        };
        return stations[faker.number().numberBetween(0, stations.length)];
    }

    public String generateDeliveryDate() {
        int daysFromNow = faker.number().numberBetween(1, 30);
        return LocalDate.now()
                .plusDays(daysFromNow)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    // ========== COURIER DATA BUILDERS ==========

    public CourierData createValidCourier() {
        return new CourierData(
                generateRandomLogin(),
                generatePassword(),
                generateCourierFirstName()
        );
    }

    public CourierData createCourierWithoutLogin() {
        return new CourierData(
                null,
                generatePassword(),
                generateCourierFirstName()
        );
    }

    public CourierData createCourierWithoutPassword() {
        return new CourierData(
                generateRandomLogin(),
                null,
                generateCourierFirstName()
        );
    }

    public CourierData createCourierWithoutFirstName() {
        return new CourierData(
                generateRandomLogin(),
                generatePassword(),
                null
        );
    }


    public OrderData createOrderWithBlackColor() {
        return createOrder(Arrays.asList("black"), "Test order with BLACK color");
    }

    public OrderData createOrderWithGreyColor() {
        return createOrder(Arrays.asList("grey"), "Test order with GREY color");
    }

    public OrderData createOrderWithBothColors() {
        return createOrder(Arrays.asList("black", "grey"), "Test order with BOTH colors");
    }

    public OrderData createOrderWithoutColor() {
        return createOrder(new ArrayList<>(), "Test order WITHOUT color");
    }


    private OrderData createOrder(java.util.List<String> colors, String comment) {
        OrderData order = new OrderData();
        order.setColor(colors);
        order.setFirstName(generateFirstName());
        order.setLastName(generateLastName());
        order.setAddress(generateAddress());
        order.setMetroStation(generateMetroStation());
        order.setPhone(generatePhone());
        order.setRentTime(faker.number().numberBetween(1, 14)); // Рандомный срок аренды 1-14 дней
        order.setDeliveryDate(generateDeliveryDate());
        order.setComment(comment);
        return order;
    }
}