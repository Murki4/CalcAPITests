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
@DisplayName("POST /")
public class PostDivide {
    @BeforeAll
    static void InstallSpec(){ //стандартные спецификации
        Specifications.Install(Specifications.requestSpec());
    }
    @AfterAll
    static void DeleteEntries() { //удаление тестовых данных из БД;
        RequestResponceEvocation.EvokDeletion();
    }
    @Test
    @Feature("POST /")
    @Story("Позитивные")
    @DisplayName("POST / с двузначными числами")
    @Description("POST запрос с оператором '/' и двузначными значениями. Возвращает верный результат" +
            "деления двух чисел с точностью до четырех знаков после разделителя")
    @Tag("Позитивные")
    @Tag("POST")
    public void PostDividePositive() {
        PostRequestBody request_body = new PostRequestBody("10","9","/");
        ResultData result = RequestResponceEvocation.Evok201(request_body);
        try { //механизм пропуска получения кол-ва знаков после запятой, если результат будет без знака точки
            String[] splitter = String.valueOf(result.getResult()).split("\\.");
            int i = splitter[1].length();
            Assertions.assertEquals(4, i); //проверка кол-ва знаков после запятой у ответа API
            String formattedDouble = String.format("%.4f", //приведение верного ответа к нужной форме
                    Double.parseDouble(request_body.getNumber_1())
                            / Double.parseDouble(request_body.getNumber_2())).replace(',', '.');
            Assertions.assertEquals(request_body.getNumber_1(), result.getNumber_1());
            Assertions.assertEquals(request_body.getNumber_2(), result.getNumber_2());
            Assertions.assertEquals(formattedDouble, result.getResult());
        }
        catch (ArrayIndexOutOfBoundsException e){
            Assertions.fail("Строка result содержит "+result.getResult());
        }
    }

    @Test
    @Feature("POST /")
    @Story("Негативные")
    @DisplayName("POST / c числами с плаваюющей точкой, %%.%")
    @Description("POST запрос с оператором '/' и двумя числами с плавающей точкой, " +
            "где после точки одна цифра. Должен вернуть код 201 и параметр result = error")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostDivideDoubleTrim() {
        RequestResponceEvocation.Evok201Negative(new PostRequestBody("13.6", "21.5", "/"));
    }

    @Test
    @Feature("POST /")
    @Story("Позитивные")
    @DisplayName("POST / отрицательные числа")
    @Description("POST с оператором '/' и двумя отрицательными двузначными числами." +
            "Возвращает верный результат деления двух чисел.")
    @Tag("Позитивные")
    @Tag("POST")
    void PostDivideNegativeNumbers() {
        PostRequestBody request_body = new PostRequestBody("-49", "-6", "/");
        ResultData result = RequestResponceEvocation.Evok201(request_body);
        try { //механизм пропуска получения кол-ва знаков после запятой, если результат будет не валидным
            String[] splitter = String.valueOf(result.getResult()).split("\\.");
            int i = splitter[1].length();
            Assertions.assertEquals(4, i); //кол-во знаков после запятой
            String formattedDouble = String.format("%.4f",
                Double.parseDouble(request_body.getNumber_1())
                        / Double.parseDouble(request_body.getNumber_2())).replace(',', '.');
            Assertions.assertEquals(request_body.getNumber_1(), result.getNumber_1());
            Assertions.assertEquals(request_body.getNumber_2(), result.getNumber_2());
            Assertions.assertEquals(formattedDouble, result.getResult());
        }
        catch (ArrayIndexOutOfBoundsException e){
            Assertions.fail("Строка result содержит "+result.getResult());
        }
    }

    @Test
    @Feature("POST /")
    @Story("Негативные")
    @DisplayName("POST / трехзначные значения")
    @Description("POST запрос с оператором / и трехзначными числами. Должен вернуть код 201 и параметр result = error")
    @Tag("POST")
    @Tag("Негативные")
    public void PostDivideNegativeThreeDigits() {
        RequestResponceEvocation.Evok201Negative(new PostRequestBody("100", "200", "/"));
    }

    @Test
    @Feature("POST /")
    @Story("Негативные")
    @DisplayName("POST / деление на 0")
    @Description("POST запрос с оператором '/' и делением двузначного числа на 0.Результатом должна быть ошибка")
    @Tag("POST")
    @Tag("Негативные")
    public void PostDivideZero() {
        RequestResponceEvocation.Evok201Negative(new PostRequestBody("35", "0", "/"),"Zero division error");
    }
}
