package ru.practicum.scooter.api;

public class ApiConstants {
    public static final String BASE_URL = "http://qa-scooter.praktikum-services.ru";
    public static final String API_PATH = "/api/v1";

    public static final String COURIER_CREATE = API_PATH + "/courier";
    public static final String COURIER_LOGIN = API_PATH + "/courier/login";
    public static final String COURIER_DELETE = API_PATH + "/courier/{id}";

    public static final String ORDER_CREATE = API_PATH + "/orders";
    public static final String ORDER_LIST = API_PATH + "/orders";
    public static final String ORDER_GET = API_PATH + "/orders/track";

    public static final int STATUS_SUCCESS = 200;
    public static final int STATUS_CREATED = 201;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_CONFLICT = 409;
    public static final int STATUS_NOT_FOUND = 404;
    public static final int STATUS_UNAUTHORIZED = 404;
}