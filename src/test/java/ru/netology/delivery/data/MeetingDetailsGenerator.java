package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class MeetingDetailsGenerator {
    private MeetingDetailsGenerator() {
    }

    @Value
    public static class ClientInfo {
        String city;
        String name;
        String phone;
    }

    public static String generateMeetingDate(int daysFromNow) {
        return LocalDate.now().plusDays(daysFromNow).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String generateValidCity(String locale) {
        String[] cities = {"Москва", "Санкт-Петербург", "Казань", "Уфа", "Волгоград", "Нижний Новгород", "Ростов-на-Дону"};
        Random random = new Random();
        return cities[random.nextInt(cities.length)];
    }

    public static String generateClientName(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String generateClientPhone(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.phoneNumber().phoneNumber();
    }

    public static class RegistrationFormFiller {
        private RegistrationFormFiller() {
        }

        public static ClientInfo generateRandomClient(String locale) {
            return new ClientInfo(
                    MeetingDetailsGenerator.generateValidCity(locale),
                    MeetingDetailsGenerator.generateClientName(locale),
                    MeetingDetailsGenerator.generateClientPhone(locale)
            );
        }
    }
}