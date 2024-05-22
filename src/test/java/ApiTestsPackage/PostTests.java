package ApiTestsPackage;

import PojoClasses.PostRequestBody;
import PojoClasses.ResultData;
import SpecificationPackage.Specifications;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Flaky;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.text.DecimalFormat;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@Epic("POST запросы")
@DisplayName("POST запросы с операторами")
public class PostTests {
    @AfterAll
    static void DeleteEntries(){ //удаление тестовых данных из БД
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(200));
        given()
                .spec(Specifications.authCred())
                .when()
                .delete()
                .then()
                .log().all();
    }
    @Test
    @DisplayName("POST + с двузначными целыми числами")
    @Description("POST запрос с оператором '+' и случайными двузначными целыми числами. Возвращает верный результат" +
            "сложения двух чисел")
    @Tag("Положительные")
    @Tag("POST")
    public void PostPlus(){
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        PostRequestBody requestBody = new PostRequestBody("+");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestBody)
                .when()
                .post()
                .then().log().all()
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestBody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestBody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(requestBody.getNumber_1()) + Double.parseDouble(requestBody.getNumber_2())),
                result.getResult());

    }
    @Test
    @DisplayName("POST + c числами с плаваюющей точкой, *.*")
    @Description("POST запрос с оператором '+' и двумя числами с плавающей точкой, " +
            "где после точки неограниченное количество цифр. Возвращает код ответа 400 и error = incorrect data")
    @Tag("POST")
    @Tag("Негативные")
    void PostPlusDoubleFull(){
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(400));
        Response response = given()
                .spec(Specifications.authCred())
                .body(new PostRequestBody("+",true))
                .when()
                .post()
                .then().log().all()
                .body("error",equalTo("incorrect data"))
                .extract().response();
    }
    @Test
    @DisplayName("POST + c числами с плаваюющей точкой, %%.%")
    @Description("POST запрос с оператором '+' и двумя числами с плавающей точкой, " +
            "где после точки одна цифра Возвращает код ответа 201 и error = error")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostPlusDoubleTrim(){
        PostRequestBody requestbody = new PostRequestBody("25.6","22.5","+");
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(result.getResult(), "error");

    }
    @Test
    @DisplayName("POST + отрицательные числа")
    @Tag("POST")
    @Tag("Положительные")
    @Description("POST с оператором '+' и двумя отрицательными числами." +
                "Результатом должен быть верный результат сложения двух чисел.")
    void PostPlusNegativeNumbersTwoDigits() {
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        PostRequestBody requestbody = new PostRequestBody("-17", "-29", "+");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(requestbody.getNumber_1()) + Double.parseDouble(requestbody.getNumber_2())),
                result.getResult());
    }
    @Test
    @DisplayName("POST + трехзначные числа")
    @Description("POST запрос с оператором + и трехзначными числами. Результатом должна быть ошибка")
    @Tag("POST")
    @Tag("Негативные")
    public void PostPlusNegativeThreeDigits(){
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        PostRequestBody requestbody = new PostRequestBody("100", "209", "+");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals("error", result.getResult());
    }
    
    @Test
    @DisplayName("POST - с двузначными числами")
    @Description("POST запрос с оператором '-' и двузначными значениями. Возвращает верный результат" +
            "вычитания двух чисел")
    @Tag("Положительные")
    @Tag("POST")
    public void PostMinus(){
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        PostRequestBody requestbody = new PostRequestBody("-");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(requestbody.getNumber_1()) - Double.parseDouble(requestbody.getNumber_2())),
                result.getResult());
    }
    @Test
    @DisplayName("POST - c числами с плаваюющей точкой, %%.*")
    @Description("POST запрос с оператором '-' и двумя числами с плавающей точкой, " +
            "где после точки неограниченное количество цифр. Возвращает код ответа 400 и error = incorrect data")
    @Tag("POST")
    @Tag("Негативные")
    void PostMinusDoubleFull(){
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(400));
        Response response = given()
                .spec(Specifications.authCred())
                .body(new PostRequestBody("-",true))
                .when()
                .post()
                .then().log().all()
                .body("error",equalTo("incorrect data"))
                .extract().response();
    }
    @Test
    @DisplayName("POST - c числами с плаваюющей точкой, %%.%")
    @Description("POST запрос с оператором '-' и двумя числами с плавающей точкой, " +
            "где после точки одна цифра Возвращает код ответа 201 и result = error")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostMinusDoubleTrim(){
        PostRequestBody requestbody = new PostRequestBody("13.6","21.5","-");
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .body("result",equalTo("error"))
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(result.getResult(), "error");
    }

    @Test
    @DisplayName("POST - отрицательные числа")
    @Description("POST с оператором '-' и двумя отрицательными двузначными числами." +
                "Должен вернуть верный результат вычитания двух чисел.")
    void PostMinusNegativeNumbers() {
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        PostRequestBody requestbody = new PostRequestBody("-23", "-54", "-");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(requestbody.getNumber_1()) - Double.parseDouble(requestbody.getNumber_2())),
                result.getResult());
    }
    @Test
    @DisplayName("POST - трехзначные значения")
    @Description("POST запрос с оператором - и трехзначными числами. Результатом должна быть ошибка")
    @Tag("POST")
    @Tag("Отрицательные")
    public void PostMinusNegativeThreeDigits(){
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        PostRequestBody requestbody = new PostRequestBody("100", "20", "-");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals("error", result.getResult());
    }

    @Test
    @DisplayName("POST * с двузначными числами")
    @Description("POST запрос с оператором '*' и двузначными значениями. Возвращает верный результат" +
            "умножения двух чисел")
    @Tag("Положительные")
    @Tag("POST")
    public void PostMulti(){
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        PostRequestBody requestbody = new PostRequestBody("*");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(requestbody.getNumber_1()) * Double.parseDouble(requestbody.getNumber_2())),
                result.getResult());
    }
    @Test
    @DisplayName("POST * c числами с плаваюющей точкой, %%.*")
    @Description("POST запрос с оператором '*' и двумя числами с плавающей точкой, " +
            "где после точки неограниченное количество цифр. Возвращает код ответа 400 и error = incorrect data")
    @Tag("POST")
    @Tag("Негативные")
    void PostMultiDoubleFull(){
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(400));
        Response response = given()
                .spec(Specifications.authCred())
                .body(new PostRequestBody("*",true))
                .when()
                .post()
                .then().log().all()
                .body("error",equalTo("incorrect data"))
                .extract().response();
    }
    @Test
    @DisplayName("POST * c числами с плаваюющей точкой, %%.%")
    @Description("POST запрос с оператором '*' и двумя числами с плавающей точкой, " +
            "где после точки одна цифра Возвращает код ответа 201 и result = error")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostMultiDoubleTrim(){
        PostRequestBody requestbody = new PostRequestBody("13.6","21.5","*");
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .body("result",equalTo("error"))
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(result.getResult(), "error");
    }

    @Test
    @DisplayName("POST * отрицательные числа")
    @Description("POST с оператором '*' и двумя отрицательными двузначными числами." +
            "Возвращает верный результат умножения двух чисел.")
    void PostMultiNegativeNumbers() {
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        PostRequestBody requestbody = new PostRequestBody("-23", "-54", "*");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(requestbody.getNumber_1()) * Double.parseDouble(requestbody.getNumber_2())),
                result.getResult());
    }
    @Test
    @DisplayName("POST * трехзначные значения")
    @Description("POST запрос с оператором * и трехзначными числами. Результатом должны быть ошибка")
    @Tag("POST")
    @Tag("Отрицательные")
    public void PostMultiNegativeThreeDigits(){
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        PostRequestBody requestbody = new PostRequestBody("100", "200", "*");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals("error", result.getResult());
    }

    @Test
    @Flaky
    @DisplayName("POST / с двузначными числами")
    @Description("POST запрос с оператором '/' и двузначными значениями. Возвращает верный результат" +
            "деления двух чисел")
    @Tag("Положительные")
    @Tag("POST")
    public void PostDivide(){
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        PostRequestBody requestbody = new PostRequestBody("/");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .extract().body().as(ResultData.class);
        String[] splitter = String.valueOf(result.getResult()).split("\\.");
        int i = splitter[1].length();
        String formattedDouble = String.format("%."+i+"f",
                Double.parseDouble(requestbody.getNumber_1())
                        / Double.parseDouble(requestbody.getNumber_2())).replace(',','.');
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(
                formattedDouble,
                result.getResult());
    }
    @Test
    @DisplayName("POST / c числами с плаваюющей точкой, %%./")
    @Description("POST запрос с оператором '/' и двумя числами с плавающей точкой, " +
            "где после точки неограниченное количество цифр. Возвращает код ответа 400 и error = incorrect data")
    @Tag("POST")
    @Tag("Негативные")
    void PostDivideDoubleFull(){
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(400));
        Response response = given()
                .spec(Specifications.authCred())
                .body(new PostRequestBody("/",true))
                .when()
                .post()
                .then().log().all()
                .body("error",equalTo("incorrect data"))
                .extract().response();
    }
    @Test
    @DisplayName("POST / c числами с плаваюющей точкой, %%.%")
    @Description("POST запрос с оператором '/' и двумя числами с плавающей точкой, " +
            "где после точки одна цифра Возвращает код ответа 201 и result = error")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostDivideDoubleTrim(){
        PostRequestBody requestbody = new PostRequestBody("13.6","21.5","/");
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .body("result",equalTo("error"))
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(result.getResult(), "error");
    }

    @Test
    @DisplayName("POST / отрицательные числа")
    @Description("POST с оператором '/' и двумя отрицательными двузначными числами." +
            "Возвращает верный результат деления двух чисел.")
    void PostDivideNegativeNumbers() {
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        PostRequestBody requestbody = new PostRequestBody("5", "15", "/");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(requestbody.getNumber_1()) / Double.parseDouble(requestbody.getNumber_2())),
                result.getResult());
    }
    @Test
    @DisplayName("POST / трехзначные значения")
    @Description("POST запрос с оператором / и трехзначными числами. Результатом должны быть ошибка")
    @Tag("POST")
    @Tag("Отрицательные")
    public void PostDivideNegativeThreeDigits(){
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        PostRequestBody requestbody = new PostRequestBody("100", "200", "/");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals("error", result.getResult());
    }
    @Test
    @DisplayName("POST / деление на 0")
    @Description("POST запрос с оператором '/' и делением двузначного числа на 0.Результатом должна быть ошибка")
    @Tag("POST")
    @Tag("Отрицательные")
    public void PostDivideZero(){
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        PostRequestBody requestbody = new PostRequestBody("35", "0", "/");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals("Zero division error", result.getResult());
    }
    @Test
    @DisplayName("POST = с двузначными числами")
    @Description("POST запрос с оператором '=' и двузначными значениями. Возвращает верный результат" +
            "деления двух чисел")
    @Tag("Положительные")
    @Tag("POST")
    public void PostEqual(){
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        PostRequestBody requestbody = new PostRequestBody("=");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(
                Boolean.toString(Double.parseDouble(requestbody.getNumber_1()) == Double.parseDouble(requestbody.getNumber_2())),
                result.getResult());
    }
    @Test
    @DisplayName("POST = c числами с плаваюющей точкой, %%.=")
    @Description("POST запрос с оператором '=' и двумя числами с плавающей точкой, " +
            "где после точки неограниченное количество цифр. Возвращает код ответа 400 и error = incorrect data")
    @Tag("POST")
    @Tag("Негативные")
    void PostEqualDoubleFull(){
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(400));
        Response response = given()
                .spec(Specifications.authCred())
                .body(new PostRequestBody("=",true))
                .when()
                .post()
                .then().log().all()
                .body("error",equalTo("incorrect data"))
                .extract().response();
    }
    @Test
    @DisplayName("POST = c числами с плаваюющей точкой, %%.%")
    @Description("POST запрос с оператором '=' и двумя числами с плавающей точкой, " +
            "где после точки одна цифра Возвращает код ответа 201 и result = error")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostEqualDoubleTrim(){
        PostRequestBody requestbody = new PostRequestBody("13.6","21.5","=");
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .body("result",equalTo("error"))
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(result.getResult(), "error");
    }

    @Test
    @DisplayName("POST = отрицательные числа")
    @Description("POST с оператором '=' и двумя отрицательными двузначными числами." +
            "Возвращает верный результат деления двух чисел.")
    void PostEqualNegativeNumbers() {
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        PostRequestBody requestbody = new PostRequestBody("-23", "-54", "=");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(
                Boolean.toString(Double.parseDouble(requestbody.getNumber_1()) == Double.parseDouble(requestbody.getNumber_2())),
                result.getResult());
    }
    @Test
    @DisplayName("POST = трехзначные значения")
    @Description("POST запрос с оператором = и трехзначными числами. Результатом должны быть ошибка")
    @Tag("POST")
    @Tag("Отрицательные")
    public void PostEqualNegativeThreeDigits(){
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(201));
        PostRequestBody requestbody = new PostRequestBody("100", "200", "=");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals("error", result.getResult());
    }
}
