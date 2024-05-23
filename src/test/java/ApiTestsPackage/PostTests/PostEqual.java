package ApiTestsPackage.PostTests;

import PojoClasses.PostRequestBody;
import PojoClasses.ResultData;
import SpecificationPackage.RequestResponceEvocation;
import SpecificationPackage.Specifications;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PostEqual {
    @BeforeAll
    static void InstallSpec(){ //стандартные спецификации
        Specifications.Install(Specifications.requestSpec());
    }
    @AfterAll
    static void DeleteEntries() { //удаление тестовых данных из БД;
        RequestResponceEvocation.EvokDeletion();
    }
    @Test
    @Feature("POST =")
    @Story("Позитивные")
    @DisplayName("POST = с двузначными числами")
    @Description("POST запрос с оператором '=' и двузначными значениями. Возвращает результат сравнения True")
    @Tag("Позитивные")
    @Tag("POST")
    public void PostEqualTrue() {
        PostRequestBody request_body = new PostRequestBody("50", "50", "=");
        ResultData result = RequestResponceEvocation.Evok201(request_body);
        String body_bool = Boolean.toString(Double.parseDouble(request_body.getNumber_1()) == Double.parseDouble(request_body.getNumber_2()));
        body_bool = body_bool.substring(0, 1).toUpperCase() + body_bool.substring(1); // в ответе True/False с большой буквы >_<
        Assertions.assertEquals(
                body_bool,
                result.getResult());
    }

    @Test
    @Feature("POST =")
    @Story("Позитивные")
    @DisplayName("POST = с двузначными числами")
    @Description("POST запрос с оператором '=' и двузначными значениями. Возвращает результат сравнения False")
    @Tag("Позитивные")
    @Tag("POST")
    public void PostEqualFalse() {
        PostRequestBody request_body = new PostRequestBody("50", "20", "=");
        ResultData result = RequestResponceEvocation.Evok201(request_body);
        String body_bool = Boolean.toString(Double.parseDouble(request_body.getNumber_1()) == Double.parseDouble(request_body.getNumber_2()));
        body_bool = body_bool.substring(0, 1).toUpperCase() + body_bool.substring(1);
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
        RequestResponceEvocation.Evok400(new PostRequestBody("s", "s", "="));
    }

    @Test
    @Feature("POST =")
    @Story("Негативные")
    @DisplayName("POST = c числами с плаваюющей точкой, %%.*")
    @Description("POST запрос с оператором '=' и двумя числами с плавающей точкой, " +
            "где после точки неограниченное количество цифр. Должен вернуть ошибку и код 400")
    @Tag("POST")
    @Tag("Негативные")
    void PostEqualDoubleFull() {
        RequestResponceEvocation.Evok400(new PostRequestBody("=", true));
    }

    @Test
    @Feature("POST =")
    @Story("Негативные")
    @DisplayName("POST = c числами с плаваюющей точкой, %%.%")
    @Description("POST запрос с оператором '=' и двумя числами с плавающей точкой, " +
            "где после точки одна цифра. Должен вернуть ошибку и код 400")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostEqualDoubleTrim() {
        PostRequestBody request_body = new PostRequestBody("13.6", "21.5", "=");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(request_body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(request_body.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(request_body.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(result.getResult(), "error");
    }

    @Test
    @Feature("POST =")
    @Story("Негативные")
    @DisplayName("POST = отрицательные числа")
    @Description("POST с оператором '=' и двумя отрицательными двузначными числами." +
            "Возвращает верный результат деления двух чисел.")
    void PostEqualNegativeNumbers() {
        PostRequestBody request_body = new PostRequestBody("-23", "-54", "=");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(request_body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(request_body.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(request_body.getNumber_2(), result.getNumber_2());
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
    @Description("POST запрос с оператором = и трехзначными числами. Результатом должны быть ошибка")
    @Tag("POST")
    @Tag("Негативные")
    public void PostEqualNegativeThreeDigits() {
        PostRequestBody request_body = new PostRequestBody("100", "200", "=");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(request_body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(request_body.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(request_body.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals("error", result.getResult());
    }
}
