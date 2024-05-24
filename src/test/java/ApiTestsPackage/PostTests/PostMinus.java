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
@DisplayName("POST -")
public class PostMinus {
    @BeforeAll
    static void InstallSpec(){ //стандартные спецификации
        Specifications.Install(Specifications.requestSpec());
    }
    @AfterAll
    static void DeleteEntries() { //удаление тестовых данных из БД;
        RequestResponceEvocation.EvokDeletion();
    }
    @Test
    @Feature("POST -")
    @Story("Позитивные")
    @DisplayName("POST - с двузначными числами")
    @Description("POST запрос с оператором '-' и двузначными значениями. Возвращает верный результат" +
            "вычитания двух чисел")
    @Tag("Позитивные")
    @Tag("POST")
    public void PostMinusPositive() {
        PostRequestBody request_body = new PostRequestBody("-");
        ResultData result = RequestResponceEvocation.Evok201(request_body);
        Assertions.assertEquals(request_body.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(request_body.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(request_body.getNumber_1()) - Double.parseDouble(request_body.getNumber_2())),
                result.getResult());
    }

    @Test
    @Feature("POST -")
    @Story("Негативные")
    @DisplayName("POST - со строками")
    @Description("POST запрос с оператором '-' и строкам. Должен вернуть код 201 и параметр result = error")
    @Tag("Негативные")
    @Tag("POST")
    public void PostMinusString() {
        RequestResponceEvocation.Evok201Negative(new PostRequestBody("s", "s", "-"));

    }

    @Test
    @Feature("POST -")
    @Story("Негативные")
    @DisplayName("POST - c числами с плаваюющей точкой, %%.%")
    @Description("POST запрос с оператором '-' и двумя числами с плавающей точкой, " +
            "где после точки одна цифра. Должен вернуть код 201 и параметр result = error")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostMinusDoubleTrim() {
        RequestResponceEvocation.Evok201Negative(new PostRequestBody("13.6", "21.5", "-"));
    }

    @Test
    @Feature("POST -")
    @Story("Позитивные")
    @DisplayName("POST - отрицательные числа")
    @Description("POST с оператором '-' и двумя отрицательными двузначными числами." +
            "Должен вернуть верный результат вычитания двух чисел.")
    @Tag("Позитивные")
    @Tag("POST")
    void PostMinusNegativeNumbers() {
        PostRequestBody request_body = new PostRequestBody("-23", "-54", "-");
        ResultData result = RequestResponceEvocation.Evok201(request_body);
        Assertions.assertEquals(request_body.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(request_body.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(request_body.getNumber_1()) - Double.parseDouble(request_body.getNumber_2())),
                result.getResult());
    }

    @Test
    @Feature("POST -")
    @Story("Негативные")
    @DisplayName("POST - трехзначные значения")
    @Description("POST запрос с оператором - и трехзначными числами. Должен вернуть код 201 и параметр result = error")
    @Tag("POST")
    @Tag("Негативные")
    public void PostMinusThreeDigits() {
        RequestResponceEvocation.Evok201Negative(new PostRequestBody("100", "20", "-"));
    }
}
