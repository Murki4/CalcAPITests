package ApiTestsPackage.PostTests;

import PojoClasses.PostRequestBody;
import PojoClasses.ResultData;
import SpecificationPackage.RequestResponceEvocation;
import SpecificationPackage.Specifications;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;

@Epic("POST запросы")
@DisplayName("POST +")
public class PostPlus {
    @BeforeAll
    static void InstallSpec() { //стандартные спецификации
        Specifications.Install(Specifications.requestSpec());
    }

    @AfterAll
    static void DeleteEntries() { //удаление тестовых данных из БД;
        RequestResponceEvocation.EvokeDeletion();
    }

    @Test()
    @Feature("POST +")
    @Story("Позитивные")
    @DisplayName("POST + с двузначными целыми числами")
    @Description("POST запрос с оператором '+' и случайными двузначными целыми числами. Возвращает верный результат" +
            "сложения двух чисел")
    @Tag("Позитивные")
    @Tag("POST")
    void PostPlusPositive() {
        PostRequestBody request_body = new PostRequestBody("+");
        ResultData result = RequestResponceEvocation.Evoke201(request_body);
        Assertions.assertTrue(result.getPk()>0);
        Assertions.assertEquals(request_body.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(request_body.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(request_body.getOperator(), result.getOperator());
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(request_body.getNumber_1()) + Double.parseDouble(request_body.getNumber_2())),
                result.getResult());
    }
    @Test
    @Feature("POST +")
    @Story("Негативные")
    @DisplayName("POST + с одним пустым значением")
    @Description("POST запрос с оператором '+' с одним пустым значением. Должен вернуть ошибку и код 400.")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostPlusEmptyNum() {
        RequestResponceEvocation.Evoke400(new PostRequestBody("","2","+"));
    }

    @Test
    @Feature("POST +")
    @Story("Негативные")
    @DisplayName("POST + со строками")
    @Description("POST запрос с оператором '+' и строкам. Должен вернуть код 201 и параметр result = error ")
    @Tag("Негативные")
    @Tag("POST")
    void PostPlusString() {
        RequestResponceEvocation.Evoke201Negative(new PostRequestBody("s", "s", "+"),"error");
    }

    @Test
    @Feature("POST +")
    @Story("Негативные")
    @DisplayName("POST + числа с плаваюющей точкой, %%.%")
    @Description("POST запрос с оператором '+' и двумя числами с плавающей точкой, " +
            "где после точки одна цифра. Должен вернуть код 201 и параметр result = error")
    @Tag("POST")
    @Tag("Негативные")
    void PostPlusDoubleTrim() {
        RequestResponceEvocation.Evoke201Negative(new PostRequestBody("25.6", "22.5", "+"),"error");
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
        ResultData result = RequestResponceEvocation.Evoke201(request_body);
        Assertions.assertTrue(result.getPk()>0);
        Assertions.assertEquals(request_body.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(request_body.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(request_body.getOperator(), result.getOperator());
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
    void PostPlusThreeDigits() {
        RequestResponceEvocation.Evoke201Negative(new PostRequestBody("100", "209", "+"),"error");
    }

    @ParameterizedTest
    @Feature("POST +")
    @ValueSource(strings = {"1","11"})
    @Story("Позитивные")
    @DisplayName("POST 1+1, 1+11")
    @Description("POST запрос со сложением 1 и 11. В первом случае должно вернуть 2, во втором 12")
    void PostPlusOne(String num2){
        PostRequestBody request_body = new PostRequestBody("1",num2,"+");
        ResultData result = RequestResponceEvocation.Evoke201(request_body);
        Assertions.assertTrue(result.getPk()>0);
        Assertions.assertEquals(request_body.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(request_body.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(request_body.getOperator(), result.getOperator());
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(request_body.getNumber_1()) + Double.parseDouble(request_body.getNumber_2())),
                result.getResult());
    }
}
