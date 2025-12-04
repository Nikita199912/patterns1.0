package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AppTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        // URL приложения, где SUT запущен.
        // Это соответствует настройкам в gradle.yml, где SUT запускается на порту 9999.
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan meeting and replan it") // Изменено название теста для ясности
    public void shouldBeSuccessfully() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysAddtoFirst = 4;
        var firstMeeting = DataGenerator.generateDate(daysAddtoFirst);
        var daysAddtoSecond = 7;
        var secondMeeting = DataGenerator.generateDate(daysAddtoSecond);

        // Ввод данных для первой встречи
        // Предполагается, что эти data-test-id атрибуты и тексты кнопок/уведомлений
        // соответствуют элементам в вашем приложении app-replan-delivery.jar
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(firstMeeting);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] ").click();
        $(byText("Запланировать")).click();

        // Проверка успешного планирования первой встречи
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + firstMeeting))
                .shouldBe(visible);

        // Перепланирование встречи на другую дату
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(secondMeeting);
        $(byText("Запланировать")).click();

        // Проверка запроса на перепланирование
        $("[data-test-id='replan-notification'] .notification__content")
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(visible);
        // Подтверждение перепланирования
        $("[data-test-id='replan-notification'] button ").click();

        // Проверка успешного перепланирования
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + secondMeeting))
                .shouldBe(visible);
    }
}