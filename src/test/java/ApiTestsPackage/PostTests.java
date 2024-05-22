package ApiTestsPackage;

import PojoClasses.PostRequestBody;
import PojoClasses.ResultData;
import SpecificationPackage.Specifications;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@Epic("POST запросы")
@DisplayName("POST запросы с операторами")
public class PostTests {
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
    @Description("POST запрос с оператором '+' и строкам. Возвращает result = error ")
    @Tag("Негативные")
    @Tag("POST")
    public void PostPlusString() {
        PostRequestBody requestbody = new PostRequestBody("s", "s", "+");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(result.getResult(), "error");
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
            "где после точки одна цифра Возвращает код ответа 201 и result = error")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostPlusDoubleTrim() {
        PostRequestBody requestbody = new PostRequestBody("25.6", "22.5", "+");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(result.getResult(), "error");

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
        PostRequestBody requestbody = new PostRequestBody("-17", "-29", "+");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(requestbody.getNumber_1()) + Double.parseDouble(requestbody.getNumber_2())),
                result.getResult());
    }

    @Test
    @Feature("POST +")
    @Story("Негативные")
    @DisplayName("POST + трехзначные числа")
    @Description("POST запрос с оператором + и трехзначными числами. Результатом должна быть ошибка")
    @Tag("POST")
    @Tag("Негативные")
    public void PostPlusNegativeThreeDigits() {
        PostRequestBody requestbody = new PostRequestBody("100", "209", "+");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals("error", result.getResult());
    }

    @Test
    @Feature("POST -")
    @Story("Позитивные")
    @DisplayName("POST - с двузначными числами")
    @Description("POST запрос с оператором '-' и двузначными значениями. Возвращает верный результат" +
            "вычитания двух чисел")
    @Tag("Позитивные")
    @Tag("POST")
    public void PostMinus() {
        PostRequestBody requestbody = new PostRequestBody("-");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(requestbody.getNumber_1()) - Double.parseDouble(requestbody.getNumber_2())),
                result.getResult());
    }

    @Test
    @Feature("POST -")
    @Story("Негативные")
    @DisplayName("POST - со строками")
    @Description("POST запрос с оператором '-' и строкам. Возвращает result = error ")
    @Tag("Негативные")
    @Tag("POST")
    public void PostMinusString() {
        PostRequestBody requestbody = new PostRequestBody("s", "s", "-");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(result.getResult(), "error");
    }

    @Test
    @Feature("POST -")
    @Story("Негативные")
    @DisplayName("POST - c числами с плаваюющей точкой, %%.*")
    @Description("POST запрос с оператором '-' и двумя числами с плавающей точкой, " +
            "где после точки неограниченное количество цифр. Возвращает код ответа 400 и error = incorrect data")
    @Tag("POST")
    @Tag("Негативные")
    void PostMinusDoubleFull() {
        given()
                .spec(Specifications.authCred())
                .body(new PostRequestBody("-", true))
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(400)
                .body("error", equalTo("incorrect data"));
    }

    @Test
    @Feature("POST -")
    @Story("Негативные")
    @DisplayName("POST - c числами с плаваюющей точкой, %%.%")
    @Description("POST запрос с оператором '-' и двумя числами с плавающей точкой, " +
            "где после точки одна цифра Возвращает код ответа 201 и result = error")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostMinusDoubleTrim() {
        PostRequestBody requestbody = new PostRequestBody("13.6", "21.5", "-");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .body("result", equalTo("error"))
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(result.getResult(), "error");
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
        PostRequestBody requestbody = new PostRequestBody("-23", "-54", "-");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(requestbody.getNumber_1()) - Double.parseDouble(requestbody.getNumber_2())),
                result.getResult());
    }

    @Test
    @Feature("POST -")
    @Story("Негативные")
    @DisplayName("POST - трехзначные значения")
    @Description("POST запрос с оператором - и трехзначными числами. Результатом должна быть ошибка")
    @Tag("POST")
    @Tag("Негативные")
    public void PostMinusNegativeThreeDigits() {
        PostRequestBody requestbody = new PostRequestBody("100", "20", "-");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals("error", result.getResult());
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
        PostRequestBody requestbody = new PostRequestBody("*");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(requestbody.getNumber_1()) * Double.parseDouble(requestbody.getNumber_2())),
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
        PostRequestBody requestbody = new PostRequestBody("s", "s", "*");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
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
        PostRequestBody requestbody = new PostRequestBody("13.6", "21.5", "*");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .body("result", equalTo("error"))
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
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
        PostRequestBody requestbody = new PostRequestBody("-23", "-54", "*");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(
                Double.toString(Double.parseDouble(requestbody.getNumber_1()) * Double.parseDouble(requestbody.getNumber_2())),
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
        PostRequestBody requestbody = new PostRequestBody("100", "200", "*");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals("error", result.getResult());
    }

    @Test
    @Flaky
    @Feature("POST /")
    @Story("Позитивные")
    @DisplayName("POST / с двузначными числами")
    @Description("POST запрос с оператором '/' и двузначными значениями. Возвращает верный результат" +
            "деления двух чисел")
    @Tag("Позитивные")
    @Tag("POST")
    public void PostDivide() {
        PostRequestBody requestbody = new PostRequestBody("10","9","/");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        String[] splitter = String.valueOf(result.getResult()).split("\\.");
        int i = splitter[1].length(); //количество знаков после запятой
        Assertions.assertEquals(4, i);
        /*String formattedDouble = String.format("%." + i + "f",
                Double.parseDouble(requestbody.getNumber_1())
                        / Double.parseDouble(requestbody.getNumber_2())).replace(',', '.');
                        хитроумный способ сравнения 14 знаков после запятой вместо 16*/
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(Double.parseDouble(requestbody.getNumber_1())
                / Double.parseDouble(requestbody.getNumber_2()), result.getResult());
    }

    @Test
    @Feature("POST /")
    @Story("Негативные")
    @DisplayName("POST / c числами с плаваюющей точкой, %%.*")
    @Description("POST запрос с оператором '/' и двумя числами с плавающей точкой, " +
            "где после точки неограниченное количество цифр. Возвращает код ответа 400 и error = incorrect data")
    @Tag("POST")
    @Tag("Негативные")
    void PostDivideDoubleFull() {
        given()
                .spec(Specifications.authCred())
                .body(new PostRequestBody("/", true))
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(400)
                .body("error", equalTo("incorrect data"));
    }

    @Test
    @Feature("POST /")
    @Story("Негативные")
    @DisplayName("POST / c числами с плаваюющей точкой, %%.%")
    @Description("POST запрос с оператором '/' и двумя числами с плавающей точкой, " +
            "где после точки одна цифра Возвращает код ответа 201 и result = error")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostDivideDoubleTrim() {
        PostRequestBody requestbody = new PostRequestBody("13.6", "21.5", "/");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .body("result", equalTo("error"))
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(result.getResult(), "error");
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
        PostRequestBody requestbody = new PostRequestBody("-49", "-6", "/");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        try { //механизм пропуска получения кол-ва знаков после запятой, если входные данные будут не числом с точкой
            String[] splitter = String.valueOf(result.getResult()).split("\\.");
            int i = splitter[1].length();
            Assertions.assertEquals(4, i);
            /*String formattedDouble = String.format("%." + i + "f",
                    Double.parseDouble(requestbody.getNumber_1())
                            / Double.parseDouble(requestbody.getNumber_2())).replace(',', '.');*/
            Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
            Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
            Assertions.assertEquals(Double.parseDouble(requestbody.getNumber_1())
                    / Double.parseDouble(requestbody.getNumber_2()), result.getResult());
        }
        catch (ArrayIndexOutOfBoundsException e){
            Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
            Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
            Assertions.assertEquals(Double.parseDouble(requestbody.getNumber_1())
                    / Double.parseDouble(requestbody.getNumber_2()), result.getResult());
        }
    }

    @Test
    @Feature("POST /")
    @Story("Негативные")
    @DisplayName("POST / трехзначные значения")
    @Description("POST запрос с оператором / и трехзначными числами. Результатом должны быть ошибка")
    @Tag("POST")
    @Tag("Негативные")
    public void PostDivideNegativeThreeDigits() {
        PostRequestBody requestbody = new PostRequestBody("100", "200", "/");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals("error", result.getResult());
    }

    @Test
    @Feature("POST /")
    @Story("Негативные")
    @DisplayName("POST / деление на 0")
    @Description("POST запрос с оператором '/' и делением двузначного числа на 0.Результатом должна быть ошибка")
    @Tag("POST")
    @Tag("Негативные")
    public void PostDivideZero() {
        PostRequestBody requestbody = new PostRequestBody("35", "0", "/");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals("Zero division error", result.getResult());
    }

    @Test
    @Feature("POST =")
    @Story("Позитивные")
    @DisplayName("POST = с двузначными числами")
    @Description("POST запрос с оператором '=' и двузначными значениями. Возвращает результат сравнения True")
    @Tag("Позитивные")
    @Tag("POST")
    public void PostEqualTrue() {
        PostRequestBody requestbody = new PostRequestBody("50", "50", "=");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        String bodybool = Boolean.toString(Double.parseDouble(requestbody.getNumber_1()) == Double.parseDouble(requestbody.getNumber_2()));
        bodybool = bodybool.substring(0, 1).toUpperCase() + bodybool.substring(1);
        Assertions.assertEquals(
                bodybool,
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
        PostRequestBody requestbody = new PostRequestBody("50", "20", "=");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        String bodybool = Boolean.toString(Double.parseDouble(requestbody.getNumber_1()) == Double.parseDouble(requestbody.getNumber_2()));
        bodybool = bodybool.substring(0, 1).toUpperCase() + bodybool.substring(1);
        Assertions.assertEquals(
                bodybool,
                result.getResult());
    }

    @Test
    @Feature("POST =")
    @Story("Негативные")
    @DisplayName("POST = со строками")
    @Description("POST запрос с оператором '=' и строкам. Возвращает result = error ")
    @Tag("Негативные")
    @Tag("POST")
    public void PostEqualString() {
        PostRequestBody requestbody = new PostRequestBody("s", "s", "=");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(result.getResult(), "error");
    }

    @Test
    @Feature("POST =")
    @Story("Негативные")
    @DisplayName("POST = c числами с плаваюющей точкой, %%.*")
    @Description("POST запрос с оператором '=' и двумя числами с плавающей точкой, " +
            "где после точки неограниченное количество цифр. Возвращает код ответа 400 и error = incorrect data")
    @Tag("POST")
    @Tag("Негативные")
    void PostEqualDoubleFull() {
        given()
                .spec(Specifications.authCred())
                .body(new PostRequestBody("=", true))
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(400)
                .body("error", equalTo("incorrect data"));
    }

    @Test
    @Feature("POST =")
    @Story("Негативные")
    @DisplayName("POST = c числами с плаваюющей точкой, %%.%")
    @Description("POST запрос с оператором '=' и двумя числами с плавающей точкой, " +
            "где после точки одна цифра. Возвращает код ответа 201 и result = error")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostEqualDoubleTrim() {
        PostRequestBody requestbody = new PostRequestBody("13.6", "21.5", "=");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals(result.getResult(), "error");
    }

    @Test
    @Feature("POST =")
    @Story("Негативные")
    @DisplayName("POST = отрицательные числа")
    @Description("POST с оператором '=' и двумя отрицательными двузначными числами." +
            "Возвращает верный результат деления двух чисел.")
    void PostEqualNegativeNumbers() {
        PostRequestBody requestbody = new PostRequestBody("-23", "-54", "=");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        String bodybool = Boolean.toString(Double.parseDouble(requestbody.getNumber_1()) == Double.parseDouble(requestbody.getNumber_2()));
        bodybool = bodybool.substring(0, 1).toUpperCase() + bodybool.substring(1);
        Assertions.assertEquals(
                bodybool,
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
        PostRequestBody requestbody = new PostRequestBody("100", "200", "=");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(requestbody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(requestbody.getNumber_1(), result.getNumber_1());
        Assertions.assertEquals(requestbody.getNumber_2(), result.getNumber_2());
        Assertions.assertEquals("error", result.getResult());
    }
}
