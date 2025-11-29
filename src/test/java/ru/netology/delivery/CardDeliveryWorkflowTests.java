package ru.netology.delivery;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.MeetingDetailsGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryWorkflowTests {

    @BeforeAll
    static void setUpAllureReporting() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAllureReporting() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setupBrowser() {
        open("http://localhost:9999/");
    }

    @Test
    void verifyCardDeliveryAndRescheduleFlow() {
        MeetingDetailsGenerator.ClientInfo client = MeetingDetailsGenerator.RegistrationFormFiller.generateRandomClient("ru");
        String initialDeliveryDate = MeetingDetailsGenerator.generateMeetingDate(3);
        String rescheduledDeliveryDate = MeetingDetailsGenerator.generateMeetingDate(7);

        $("[data-test-id='city'] input").setValue(client.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(initialDeliveryDate);
        $("[data-test-id='name'] input").setValue(client.getName());
        $("[data-test-id='phone'] input").setValue(client.getPhone());
        $("[data-test-id='agreement']").click();
        $("button.button").shouldHave(exactText("Запланировать")).click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно!"));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(visible)
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + initialDeliveryDate));
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(rescheduledDeliveryDate);
        $("button.button").shouldHave(exactText("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__title")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Необходимо подтверждение"));
        $("[data-test-id='replan-notification'] button").shouldHave(exactText("Перепланировать")).click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно!"));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(visible)
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + rescheduledDeliveryDate));
    }
}