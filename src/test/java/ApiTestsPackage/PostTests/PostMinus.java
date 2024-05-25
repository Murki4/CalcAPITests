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
        RequestResponceEvocation.EvokeDeletion();
    }
    @Test
    @Feature("POST -")
    @Story("Позитивные")
    @DisplayName("POST - с двузначными числами")
    @Description("POST запрос с оператором '-' и двузначными значениями. Возвращает верный результат" +
            "вычитания двух чисел")
    @Tag("Позитивные")
    @Tag("POST")
    void PostMinusPositive() {
        PostRequestBody request_body = new PostRequestBody("-");
        ResultData result = RequestResponceEvocation.Evoke201(request_body);
        Assertions.assertTrue(result.getPk()>0);
        Assertions.assertEquals(request_body.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(request_body.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(request_body.getOperator(), result.getOperator());
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(request_body.getNumber_1()) - Double.parseDouble(request_body.getNumber_2())),
                result.getResult());
    }

    @Test
    @Feature("POST -")
    @Story("Негативные")
    @DisplayName("POST + с одним пустым значением")
    @Description("POST запрос с оператором '-' с одним пустым значением. Должен вернуть ошибку и код 400.")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostMinusEmptyNum() {
        RequestResponceEvocation.Evoke400(new PostRequestBody("","2","-"));
    }

    @Test
    @Feature("POST -")
    @Story("Негативные")
    @DisplayName("POST - со строками")
    @Description("POST запрос с оператором '-' и строкам. Должен вернуть код 201 и параметр result = error")
    @Tag("Негативные")
    @Tag("POST")
    void PostMinusString() {
        RequestResponceEvocation.Evoke201Negative(new PostRequestBody("s", "f", "-"),"error");

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
        RequestResponceEvocation.Evoke201Negative(new PostRequestBody("13.6", "21.5", "-"),"error");
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
        ResultData result = RequestResponceEvocation.Evoke201(request_body);
        Assertions.assertTrue(result.getPk()>0);
        Assertions.assertEquals(request_body.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(request_body.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(request_body.getOperator(), result.getOperator());
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
    void PostMinusThreeDigits() {
        RequestResponceEvocation.Evoke201Negative(new PostRequestBody("100", "203", "-"),"error");
    }
}
