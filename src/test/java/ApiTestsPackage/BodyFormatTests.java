package ApiTestsPackage;

import PojoClasses.ResultData;
import SpecificationPackage.RequestResponceEvocation;
import SpecificationPackage.Specifications;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("POST запросы")
@DisplayName("POST JSON")
public class BodyFormatTests {
    //Пришлось отказаться от сокращений из RequestResponceEvocation.
    //В дебагере видно что передается переменная типа File, но в запросе летит строка пути до файла а не сам файл
    @BeforeAll
    static void InstallSpec(){ //стандартные спецификации
        Specifications.Install(Specifications.requestSpec());
    }

    @AfterAll
    static void DeleteEntries() { //удаление тестовых данных из БД;
        RequestResponceEvocation.EvokeDeletion();
    }

    @Test
    @Feature("Правильность обработки тела")
    @Story("Позитивные")
    @DisplayName("BODY mixed")
    @Description("POST запрос с оператором '+', в теле запроса переставлены местами ожидаемые параметры. " +
            "Должен вепрнуть результат сложения двух чисел")
    @Tag("Позитивные")
    @Tag("POST")
    void PostMixedBody() {
        File json = new File("./src/test/java/JSONFiles/MixedBody.json");
        ResultData result = given()
                .spec(Specifications.authCred())
                .body(json)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
        try {
            Assertions.assertTrue(result.getPk()>0);
            Assertions.assertEquals(JsonPath.read(json, "number_1"), result.getNumber_1());
            Assertions.assertEquals(JsonPath.read(json, "number_2"), result.getNumber_2());
            Assertions.assertEquals(JsonPath.read(json, "operator"), result.getOperator());
            Assertions.assertEquals(
                    Double.toString(
                            Double.parseDouble(JsonPath.read(json, "number_1")) +
                            Double.parseDouble(JsonPath.read(json, "number_2"))),
                            result.getResult());
        }
        catch(Exception e) {
            Assertions.fail("Не удалось конвертировать значение из json файла");
        }
    }

    @Test
    @Story("Негативные")
    @Feature("Правильность обработки тела")
    @DisplayName("BODY Upper reg parameters")
    @Description("POST запрос с оператором '+', в теле запроса наименование параметра 'operator' большими буквами. " +
            "В целях соблюдения правильности запроса должен вернуть код 400")
    @Tag("Негативные")
    @Tag("Исследовательские")
    @Tag("POST")
    void PostUpperBody() {
        File json = new File("./src/test/java/JSONFiles/UpperBody.json");
        given()
                .spec(Specifications.authCred())
                .body(json)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(400)
                .body("error", equalTo("incorrect data"));
    }

    @Test
    @Story("Негативные")
    @Feature("Правильность обработки тела")
    @DisplayName("BODY garbage data")
    @Description("POST запрос с оператором '+', в файле json содержится невалидное json тело. " +
            "Должен вернуться код 400")
    @Tag("Негативные")
    @Tag("POST")
    void PostGarbageData() {
        File json = new File("./src/test/java/JSONFiles/GarbageBody.json");
        given()
                .spec(Specifications.authCred())
                .body(json)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(400)
                .body("detail", containsString("JSON parse error"));
    }
    @Test
    @Story("Негативные")
    @Feature("Правильность обработки тела")
    @DisplayName("BODY additional param")
    @Description("POST запрос с оператором '+', в теле запроса содержится лишний параметр" +
            "В целях соблюдения правильности запроса должен вернуть код 400")
    @Tag("Негативные")
    @Tag("POST")
    void PostAdditionalParam() {
        File json = new File("./src/test/java/JSONFiles/AddParamBody.json");
        given()
                .spec(Specifications.authCred())
                .body(json)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(400)
                .body("error", equalTo("incorrect data"));
    }
}
