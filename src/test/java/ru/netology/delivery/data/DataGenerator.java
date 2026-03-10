package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import com.github.javafaker.PhoneNumber;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private DataGenerator() {
    }

    public static String generateDate(int shift) {
        return LocalDate.now().plusDays((long)shift).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
    public static String generateCity() {
        String[] cities = new String[] {
                "Брянск", "Владимир", "Воронеж", "Иваново", "Калуга", "Кострома", "Курск",
                "Липецк", "Орел", "Рязань", "Смоленск", "Тамбов", "Тверь", "Тула", "Ярославль",
                "Архангельск", "Вологда", "Калининград", "Петрозаводск", "Сыктывкар",
                "Санкт-Петербург", "Мурманск", "Салехард", "Великий Новгород", "Псков", "Уфа",
                "Киров", "Йошкар-Ола", "Саранск", "Нижний Новгород", "Оренбург", "Пенза", "Пермь",
                "Самара", "Саратов", "Казань", "Ижевск", "Ульяновск", "Чебоксары", "Курган",
                "Екатеринбург", "Тюмень", "Челябинск", "Салехард", "Горно-Алтайск", "Барнаул",
                "Улан-Удэ", "Чита", "Иркутск", "Кемерово", "Красноярск", "Хабаровск"};
        return cities[new Random().nextInt(cities.length)];
    }

    public static String generateName(String locale) {
        var faker = new Faker(new Locale(locale));
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String generatePhone(String locale) {
        var faker = new Faker(new Locale(locale));
        return faker.phoneNumber().phoneNumber();
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String locale) {
            return new UserInfo(generateCity(), generateName(locale), generatePhone(locale));
        }
    }
    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}