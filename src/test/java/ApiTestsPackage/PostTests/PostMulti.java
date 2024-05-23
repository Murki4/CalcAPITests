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
public class PostMulti {
    @BeforeAll
    static void InstallSpec(){ //стандартные спецификации
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
    @Feature("POST *")
    @Story("Позитивные")
    @DisplayName("POST * с двузначными числами")
    @Description("POST запрос с оператором '*' и двузначными значениями. Возвращает верный результат" +
            "умножения двух чисел")
    @Tag("Позитивные")
    @Tag("POST")
    public void PostMulti() {
        PostRequestBody request_body = new PostRequestBody("*");
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
                Double.toString(Double.parseDouble(request_body.getNumber_1()) * Double.parseDouble(request_body.getNumber_2())),
                result.getResult());
    }

    @Test
    @Feature("POST *")
    @Story("Негативные")
    @DisplayName("POST * со строками")
    @Description("POST запрос с оператором '*' и строкам. Возвращает result = error ")
    @Tag("Негативные")
    @Tag("POST")
    public void PostMultiString() {
        PostRequestBody request_body = new PostRequestBody("s", "s", "*");
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
    @Feature("POST *")
    @Story("Негативные")
    @DisplayName("POST * c числами с плаваюющей точкой, %%.*")
    @Description("POST запрос с оператором '*' и двумя числами с плавающей точкой, " +
            "где после точки неограниченное количество цифр. Возвращает код ответа 400 и error = incorrect data")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostMultiDoubleFull() {
        given()
                .spec(Specifications.authCred())
                .body(new PostRequestBody("*", true))
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(400)
                .body("error", equalTo("incorrect data"));
    }

    @Test
    @Feature("POST *")
    @Story("Негативные")
    @DisplayName("POST * c числами с плаваюющей точкой, %%.%")
    @Description("POST запрос с оператором '*' и двумя числами с плавающей точкой, " +
            "где после точки одна цифра Возвращает код ответа 201 и result = error")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostMultiDoubleTrim() {
        PostRequestBody request_body = new PostRequestBody("13.6", "21.5", "*");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(request_body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .body("result", equalTo("error"))
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(request_body.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(request_body.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(result.getResult(), "error");
    }

    @Test
    @Feature("POST *")
    @Story("Позитивные")
    @DisplayName("POST * отрицательные числа")
    @Description("POST с оператором '*' и двумя отрицательными двузначными числами." +
            "Возвращает верный результат умножения двух чисел.")
    @Tag("Позитивные")
    @Tag("POST")
    void PostMultiNegativeNumbers() {
        PostRequestBody request_body = new PostRequestBody("-23", "-54", "*");
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
                Double.toString(Double.parseDouble(request_body.getNumber_1()) * Double.parseDouble(request_body.getNumber_2())),
                result.getResult());
    }

    @Test
    @Feature("POST *")
    @Story("Негативные")
    @DisplayName("POST * трехзначные значения")
    @Description("POST запрос с оператором * и трехзначными числами. Результатом должны быть ошибка")
    @Tag("POST")
    @Tag("Негативные")
    public void PostMultiNegativeThreeDigits() {
        PostRequestBody request_body = new PostRequestBody("100", "200", "*");
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
