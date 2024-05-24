package ApiTestsPackage.PostTests;

import PojoClasses.PostRequestBody;
import PojoClasses.ResultData;
import SpecificationPackage.RequestResponceEvocation;
import SpecificationPackage.Specifications;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;

@Epic("POST запросы")
@DisplayName("POST =")
public class PostEqual {
    @BeforeAll
    static void InstallSpec(){ //стандартные спецификации
        Specifications.Install(Specifications.requestSpec());
    }
    @AfterAll
    static void DeleteEntries() { //удаление тестовых данных из БД;
        RequestResponceEvocation.EvokeDeletion();
    }
    @Test
    @Feature("POST =")
    @Story("Позитивные")
    @DisplayName("POST = с двузначными числами true")
    @Description("POST запрос с оператором '=' и двузначными значениями. Возвращает результат сравнения True")
    @Tag("Позитивные")
    @Tag("POST")
    public void PostEqualTrue() {
        PostRequestBody request_body = new PostRequestBody("50", "50.0", "=");
        ResultData result = RequestResponceEvocation.Evoke201(request_body);
        String body_bool = Boolean.toString(
                Double.parseDouble(request_body.getNumber_1()) == Double.parseDouble(request_body.getNumber_2()));
        body_bool = body_bool.substring(0, 1).toUpperCase() + body_bool.substring(1); // в ответе True/False с большой буквы >_<
        Assertions.assertTrue(result.getPk()>0);
        Assertions.assertEquals(request_body.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(request_body.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(request_body.getOperator(), result.getOperator());
        Assertions.assertEquals(
                body_bool,
                result.getResult());
    }

    @Test
    @Feature("POST =")
    @Story("Позитивные")
    @DisplayName("POST = с двузначными числами false")
    @Description("POST запрос с оператором '=' и двузначными значениями. Возвращает результат сравнения False")
    @Tag("Позитивные")
    @Tag("POST")
    public void PostEqualFalse() {
        PostRequestBody request_body = new PostRequestBody("50", "20", "=");
        ResultData result = RequestResponceEvocation.Evoke201(request_body);
        String body_bool = Boolean.toString(
                Double.parseDouble(request_body.getNumber_1()) == Double.parseDouble(request_body.getNumber_2()));
        body_bool = body_bool.substring(0, 1).toUpperCase() + body_bool.substring(1);
        Assertions.assertTrue(result.getPk()>0);
        Assertions.assertEquals(request_body.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(request_body.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(request_body.getOperator(), result.getOperator());
        Assertions.assertEquals(
                body_bool,
                result.getResult());
    }

    @Test
    @Feature("POST =")
    @Story("Негативные")
    @DisplayName("POST = со строками")
    @Description("POST запрос с оператором '=' и строкам. Должен вернуть ошибку и код 400 ")
    @Tag("Негативные")
    @Tag("POST")
    public void PostEqualString() {
        RequestResponceEvocation.Evoke201Negative(new PostRequestBody("s", "s", "="));
    }

    @Test
    @Feature("POST =")
    @Story("Негативные")
    @DisplayName("POST = c числами с плаваюющей точкой, %%.%")
    @Description("POST запрос с оператором '=' и двумя числами с плавающей точкой, " +
            "где после точки одна цифра. Должен вернуть код 201 и параметр result = error")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostEqualDoubleTrim() {
        RequestResponceEvocation.Evoke201Negative(new PostRequestBody("13.6", "21.5", "="));
    }

    @Test
    @Feature("POST =")
    @Story("Негативные")
    @DisplayName("POST = отрицательные числа")
    @Description("POST с оператором '=' и двумя отрицательными двузначными числами." +
            "Должен вернуть результат сравнения False.")
    void PostEqualNegativeNumbers() {
        PostRequestBody request_body = new PostRequestBody("-23", "-54", "=");
        ResultData result = RequestResponceEvocation.Evoke201(request_body);
        Assertions.assertTrue(result.getPk()>0);
        Assertions.assertEquals(request_body.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(request_body.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(request_body.getOperator(), result.getOperator());
        String body_bool = Boolean.toString(Double.parseDouble(request_body.getNumber_1()) == Double.parseDouble(request_body.getNumber_2()));
        body_bool = body_bool.substring(0, 1).toUpperCase() + body_bool.substring(1);
        Assertions.assertEquals(
                body_bool,
                result.getResult());
    }

    @Test
    @Feature("POST =")
    @Story("Негативные")
    @DisplayName("POST = трехзначные значения")
    @Description("POST запрос с оператором = и трехзначными числами. Должен вернуть код 201 и параметр result = error")
    @Tag("POST")
    @Tag("Негативные")
    public void PostEqualNegativeThreeDigits() {
        RequestResponceEvocation.Evoke201Negative(new PostRequestBody("100", "200", "="));
    }
}
