package ru.netology;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;


import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;



public class CardDeliveryTest {

    @BeforeEach
    public void openPage() {
        open("http://localhost:9999/");
    }
    //Код для генерации строк с датами можно было вынести в отдельный метод,
    // передавать в него количество дней и паттерн для форматирования, а возвращать отформатированную строку для использования в тесте.
    // Так можно получить универсальный метод для подготовки тестовых данных.
    public String generateDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }
    String planningDate = generateDate(3, "dd.MM.yyyy");
    @Test
    void shouldCheckCorrectForm() {
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        // Для отправки нажатия клавиши Delete в качестве аргумента в метод sendKeys Вы можете передавать Keys.DELETE.
        // Класс Keys находится в пакете org.openqa.selenium.
        $x("//input[@type=\"tel\"]").doubleClick().sendKeys(Keys.DELETE);
        $x("//input[@placeholder=\"Дата встречи\"]").val(planningDate);
        $("[data-test-id='name'] input").val("Иван Смирнов");
        $("[data-test-id='phone'] input").val("+79118888888");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $x("//div[@class=\"notification__title\"]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__content").should(exactText("Встреча успешно забронирована на " + planningDate));

    }

    @Test
    void shouldCheckIncorrectCity() {
        $x("//input[@placeholder=\"Город\"]").val("Винтерфелл");
        $x("//input[@type=\"tel\"]").doubleClick().sendKeys(Keys.DELETE);
        $x("//input[@placeholder=\"Дата встречи\"]").val(planningDate);
        $("[data-test-id='name'] input").val("Иван Смирнов");
        $("[data-test-id='phone'] input").val("+79118888888");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id=city] .input__sub").should(exactText("Доставка в выбранный город недоступна"));

    }

    @Test
    void shouldCheckNameEmpty() {
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@type=\"tel\"]").doubleClick().sendKeys(Keys.DELETE);
        $x("//input[@placeholder=\"Дата встречи\"]").val(planningDate);
        $("[data-test-id='name'] input").val("");
        $("[data-test-id='phone'] input").val("+79118888888");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id=name] .input__sub").should(exactText("Поле обязательно для заполнения"));

    }

    @Test
    void shouldCheckInvalidName() {
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@type=\"tel\"]").doubleClick().sendKeys(Keys.DELETE);
        $x("//input[@placeholder=\"Дата встречи\"]").val(planningDate);
        $("[data-test-id='name'] input").val("Jon Snow");
        $("[data-test-id='phone'] input").val("+79118888888");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id=name] .input__sub").should(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));

    }


    @Test
    void shouldCheckInvalidDate() {
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@type=\"tel\"]").doubleClick().sendKeys(Keys.DELETE);
        String meetingDate = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//input[@placeholder=\"Дата встречи\"]").val(meetingDate);
        $("[data-test-id='name'] input").val("Иван Смирнов");
        $("[data-test-id='phone'] input").val("+79118888888");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id=date] .input__sub").should(exactText("Заказ на выбранную дату невозможен"));
    }


    @Test
    void shouldCheckInvalidPhone() {
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@type=\"tel\"]").doubleClick().sendKeys(Keys.DELETE);
        $x("//input[@placeholder=\"Дата встречи\"]").val(planningDate);
        $("[data-test-id='name'] input").val("Иван Смирнов");
        $("[data-test-id='phone'] input").val("+7911888888");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id=phone] .input__sub").should(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));

    }

    @Test
    void shouldCheckEmptyPhone() {
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@type=\"tel\"]").doubleClick().sendKeys(Keys.DELETE);
        $x("//input[@placeholder=\"Дата встречи\"]").val(planningDate);
        $("[data-test-id='name'] input").val("Иван Смирнов");
        $("[data-test-id='phone'] input").val("");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id=phone] .input__sub").should(exactText("Поле обязательно для заполнения"));
    }


    @Test
    void shouldMissClickAgreement() {
        $x("//input[@placeholder=\"Город\"]").val("Санкт-Петербург");
        $x("//input[@type=\"tel\"]").doubleClick().sendKeys(Keys.DELETE);
        $x("//input[@placeholder=\"Дата встречи\"]").val(planningDate);
        $("[data-test-id='name'] input").val("Иван Смирнов");
        $("[data-test-id='phone'] input").val("+79118888888");
        $x("//*[contains(text(),'Забронировать')]").click();
        //  $x("//div[@class=\"notification__title\"]").shouldBe(visible, Duration.ofSeconds(10));
        $("[data-test-id=agreement] .checkbox__text").should(exactText("Я соглашаюсь с условиями обработки и использования  моих персональных данных"));


    }
}
