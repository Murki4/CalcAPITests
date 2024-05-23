package ApiTestsPackage.PostTests;

import PojoClasses.PostRequestBody;
import PojoClasses.ResultData;
import SpecificationPackage.Specifications;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Epic("POST запросы")
public class PostPlus {
    @BeforeAll
    static void InstallSpec() { //стандартные спецификации
        Specifications.Install(Specifications.requestSpec());
    }

    @AfterAll
    static void DeleteEntries() { //удаление тестовых данных из БД;
        given()
                .spec(Specifications.authCred())
                .when()
                .delete()
                .then()
                .log().all();
    }

    @Test
    @Feature("POST +")
    @Story("Позитивные")
    @DisplayName("POST + с двузначными целыми числами")
    @Description("POST запрос с оператором '+' и случайными двузначными целыми числами. Возвращает верный результат" +
            "сложения двух чисел")
    @Tag("Позитивные")
    @Tag("POST")
    public void PostPlus() {
        PostRequestBody requestBody = new PostRequestBody("+");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestBody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestBody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestBody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(requestBody.getNumber_1()) + Double.parseDouble(requestBody.getNumber_2())),
                result.getResult());
    }

    @Test
    @Feature("POST +")
    @Story("Негативные")
    @DisplayName("POST + со строками")
    @Description("POST запрос с оператором '+' и строкам. Должна вернуть код 400 ")
    @Tag("Негативные")
    @Tag("POST")
    public void PostPlusString() {
        PostRequestBody request_body = new PostRequestBody("s", "s", "+");
        given()
                .spec(Specifications.authCred())
                .body(request_body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(400)
                .body("error", equalTo("incorrect data"));

    }

    @Test
    @Feature("POST +")
    @Story("Негативные")
    @DisplayName("POST + c числами с плаваюющей точкой, %%.*")
    @Description("POST запрос с оператором '+' и двумя числами с плавающей точкой, " +
            "где после точки неограниченное количество цифр. Возвращает код ответа 400 и error = incorrect data")
    @Tag("POST")
    @Tag("Негативные")
    void PostPlusDoubleFull() {
        given()
                .spec(Specifications.authCred())
                .body(new PostRequestBody("+", true))
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(400)
                .body("error", equalTo("incorrect data"));
    }

    @Test
    @Feature("POST +")
    @Story("Негативные")
    @DisplayName("POST + c числами с плаваюющей точкой, %%.%")
    @Description("POST запрос с оператором '+' и двумя числами с плавающей точкой, " +
            "где после точки одна цифра. Должна быть ошибка и вернуть код 400")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostPlusDoubleTrim() {
        PostRequestBody request_body = new PostRequestBody("25.6", "22.5", "+");
        given()
                .spec(Specifications.authCred())
                .body(request_body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(400)
                .body("error", equalTo("incorrect data"));

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
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(request_body.getNumber_1()) + Double.parseDouble(request_body.getNumber_2())),
                result.getResult());
    }

    @Test
    @Feature("POST +")
    @Story("Негативные")
    @DisplayName("POST + трехзначные числа")
    @Description("POST запрос с оператором + и трехзначными числами. должен вернуть ошибку и код 400")
    @Tag("POST")
    @Tag("Негативные")
    public void PostPlusNegativeThreeDigits() {
        PostRequestBody request_body = new PostRequestBody("100", "209", "+");
        given()
                .spec(Specifications.authCred())
                .body(request_body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(400)
                .body("error", equalTo("incorrect data"));
    }
}
