package ru.practicum.scooter.utils;

import java.util.Random;

public class TestDataGenerator {
    private static final Random random = new Random();
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final String NAMES = "Иван;Петр;Сергей;Анна;Мария;Ольга;Елена;Дмитрий;Алексей;Павел";
    private static final String LAST_NAMES = "Иванов;Петров;Сидоров;Смирнов;Морозов;Волков;Соколов;Лебедев;Козлов;Новиков";

    public static String generateRandomLogin() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static String generatePassword() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static String generateFirstName() {
        String[] names = NAMES.split(";");
        return names[random.nextInt(names.length)];
    }

    public static String generateLastName() {
        String[] lastNames = LAST_NAMES.split(";");
        return lastNames[random.nextInt(lastNames.length)];
    }

    public static String generatePhone() {
        StringBuilder sb = new StringBuilder("+7");
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static String generateAddress() {
        String[] streets = {"Невский проспект", "Марсова поля", "Садовая улица", "Декабристов", "Рубинштейна"};
        String[] buildings = {"10", "25", "45", "73", "100"};
        return streets[random.nextInt(streets.length)] + ", " + buildings[random.nextInt(buildings.length)];
    }
}