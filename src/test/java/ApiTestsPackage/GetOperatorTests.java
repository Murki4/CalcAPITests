package ApiTestsPackage;

import PojoClasses.PostRequestBody;
import PojoClasses.ResultData;
import SpecificationPackage.RequestResponceEvocation;
import SpecificationPackage.Specifications;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.given;

@Epic("GET запросы")
@DisplayName("GET запросы с параметром operator")
public class GetOperatorTests {
    @BeforeAll
    static void InstallSpecAndMakeEntries() {
        Specifications.Install(Specifications.requestSpec());
        String[] opers = {"+", "-", "*", "/", "="};
        for (String oper : opers)
            for (int j = 0; j < 2; j++) {
                given()
                        .spec(Specifications.authCred())
                        .body(new PostRequestBody(oper))
                        .when()
                        .post()
                        .then().log().all();
            }
    }

    @AfterAll
    static void DeleteEntries() { //удаление тестовых данных
        RequestResponceEvocation.EvokeDeletion();
    }

    @Test
    @Feature("Позитивные")
    @DisplayName("GET пустой")
    @Description("GET запрос без параметров. Должен вернуть код 200 и список 10 опаераций пользователя")
    @Tag("Позитивные")
    @Tag("GET")
    public void GetEmpty() {
        List<ResultData> entries = given()
                .spec(Specifications.authCred())
                .when()
                .get()
                .then().log().all()
                .statusCode(200)
                .extract().body().jsonPath().getList("",ResultData.class);
        Assertions.assertNotNull(entries);
        Assertions.assertEquals(10,entries.size());
        for(ResultData l : entries){
            Assertions.assertNotEquals("", l.getNumber_1());
            Assertions.assertNotEquals("", l.getNumber_2());
            Assertions.assertNotEquals("", l.getOperator());
            Assertions.assertNotEquals("", l.getResult());
        }


    }

    @Test
    @Feature("Негативные")
    @DisplayName("GET operator")
    @Description("GET запрос с параметром operators без указания значения. " +
            "Должен вернуть код 400 и error = not supported operator")
    @Tag("Негативные")
    @Tag("GET")
    public void OperatorsEmpty() {
        given()
                .spec(Specifications.authCred())
                .when()
                .get("?operator=")
                .then().log().all()
                .statusCode(400)
                .body("error", equalTo("not supported operator"));
    }

    @ParameterizedTest
    @Feature("Позитивные")
    @ValueSource(strings = {"+", "-", "*", "/", "%3D"})
    @DisplayName("GET operator parametrized")
    @Description("GET запрос с параметром operators и одним из операторов(+, -, *, /, =). " +
            "Должен вернуть код 200 и список операций с данным оператором")
    @Tag("Позитивные")
    @Tag("GET")
    public void OperatorsSome(String oper) {
        List<ResultData> entries = given()
                .spec(Specifications.authCred())
                .urlEncodingEnabled(false)
                .when()
                .get("?operator={value}", oper)
                .then().log().all()
                .statusCode(200)
                .extract().body().jsonPath().getList("", ResultData.class);
        if (oper.equals("%3D")) {
            oper = "=";
        }
        for (ResultData entry : entries) {
            Assertions.assertNotEquals("", entry.getNumber_1());
            Assertions.assertNotEquals("", entry.getNumber_2());
            Assertions.assertEquals(entry.getOperator(), oper);
            Assertions.assertNotEquals("", entry.getResult());
        }
    }

    @Test
    @Feature("Негативные")
    @DisplayName("GET operator случайное значение")
    @Description("GET запрос с параметром operators и неподдерживаемым арифметическим оператором. " +
            "Должен вернуть код 400 и ошибку error = not supported operator")
    @Tag("Негативные")
    @Tag("GET")
    public void OperatorNegative() {
        given()
                .spec(Specifications.authCred())
                .when()
                .get("?operator=^")
                .then().log().all()
                .statusCode(400)
                .body("error", equalTo("not supported operator"));
    }
}
