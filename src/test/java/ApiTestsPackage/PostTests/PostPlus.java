package ApiTestsPackage.PostTests;

import PojoClasses.PostRequestBody;
import PojoClasses.ResultData;
import SpecificationPackage.RequestResponceEvocation;
import SpecificationPackage.Specifications;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@Epic("POST запросы")
@DisplayName("POST -")
public class PostPlus {
    @BeforeAll
    static void InstallSpec() { //стандартные спецификации
        Specifications.Install(Specifications.requestSpec());
    }

    @AfterAll
    static void DeleteEntries() { //удаление тестовых данных из БД;
        RequestResponceEvocation.EvokDeletion();
    }

    @Test
    @Feature("POST +")
    @Story("Позитивные")
    @DisplayName("POST + с двузначными целыми числами")
    @Description("POST запрос с оператором '+' и случайными двузначными целыми числами. Возвращает верный результат" +
            "сложения двух чисел")
    @Tag("Позитивные")
    @Tag("POST")
    public void PostPlusPositive() {
        PostRequestBody request_body = new PostRequestBody("+");
        ResultData result = RequestResponceEvocation.Evok201(request_body);
        Assertions.assertEquals(request_body.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(request_body.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(request_body.getNumber_1()) + Double.parseDouble(request_body.getNumber_2())),
                result.getResult());
    }

    @Test
    @Feature("POST +")
    @Story("Негативные")
    @DisplayName("POST + со строками")
    @Description("POST запрос с оператором '+' и строкам. Должен вернуть код 201 и параметр result = error ")
    @Tag("Негативные")
    @Tag("POST")
    public void PostPlusString() {
        RequestResponceEvocation.Evok201Negative(new PostRequestBody("s", "s", "+"));
    }

    @Test
    @Feature("POST +")
    @Story("Негативные")
    @DisplayName("POST + c числами с плаваюющей точкой, %%.%")
    @Description("POST запрос с оператором '+' и двумя числами с плавающей точкой, " +
            "где после точки одна цифра. Должен вернуть код 201 и параметр result = error")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostPlusDoubleTrim() {
        RequestResponceEvocation.Evok201Negative(new PostRequestBody("25.6", "22.5", "+"));
    }

    @Test
    @Feature("POST +")
    @Story("Позитивные")
    @DisplayName("POST + отрицательные числа")
    @Tag("POST")
    @Tag("Позитивные")
    @Description("POST с оператором '+' и двумя отрицательными числами." +
            "Результатом должен быть верный результат сложения двух чисел.")
    void PostPlusNegativeNumbersTwoDigits() {
        PostRequestBody request_body = new PostRequestBody("-17", "-29", "+");
        ResultData result = RequestResponceEvocation.Evok201(request_body);
        Assertions.assertEquals(request_body.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(request_body.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(request_body.getNumber_1()) + Double.parseDouble(request_body.getNumber_2())),
                result.getResult());
    }

    @Test
    @Feature("POST +")
    @Story("Негативные")
    @DisplayName("POST + трехзначные числа")
    @Description("POST запрос с оператором + и трехзначными числами. Должен вернуть код 201 и параметр result = error")
    @Tag("POST")
    @Tag("Негативные")
    public void PostPlusThreeDigits() {
        RequestResponceEvocation.Evok201Negative(new PostRequestBody("100", "209", "+"));
    }
}
