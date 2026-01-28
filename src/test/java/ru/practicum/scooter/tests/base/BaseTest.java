package ru.practicum.scooter.tests.base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import ru.practicum.scooter.api.ApiConstants;

public class BaseTest {
    protected static RequestSpecification requestSpec;

    @BeforeClass
    public static void setupBase() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(ApiConstants.BASE_URL)
                .setContentType("application/json")
                .addFilter(new AllureRestAssured())
                .build();
    }
}
