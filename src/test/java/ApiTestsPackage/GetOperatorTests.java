package ApiTestsPackage;

import PojoClasses.PostRequestBody;
import PojoClasses.ResultData;
import SpecificationPackage.Specifications;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.hamcrest.Matchers.equalTo;
import java.util.List;

import static io.restassured.RestAssured.given;

@Epic("GET запросы")
@DisplayName("GET запросы с параметром operator")
public class GetOperatorTests {
    @BeforeAll
    static void InstallSpec(){ //установка стандартных спецификаций и создание набора данных для теста
        Specifications.Install(Specifications.requestSpec());
        String[] opers = {"+","-","*","/","="};
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
    static void DeleteEntries(){ //удаление тестовых данных
        given()
                .spec(Specifications.authCred())
                .when()
                .delete()
                .then()
                .log().all();
    }

    @Test
    @DisplayName("GET operator")
    @Description("GET запрос с параметром operators. Возвращает список всех совершенных операций пользователя")
    @Tag("Положительные")
    @Tag("GET")
    public void OperatorsAll(){
        List<ResultData> entries = given()
                .spec(Specifications.authCred())
                .when()
                .get("?operators")
                .then().log().all()
                .statusCode(200)
                .extract().body().jsonPath().getList("",ResultData.class);
        Assertions.assertNotNull(entries);
        Assertions.assertEquals(10,entries.size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"+","-","*","/","%3D"})
    @DisplayName("GET operator parametrized")
    @Description("GET запрос с параметром operators и одним из операторов(+, -, *, /, =)")
    @Tag("Положительные")
    @Tag("GET")
    public void OperatorsSome(String oper){
        List<ResultData> entries = given()
                .spec(Specifications.authCred())
                .urlEncodingEnabled(false)
                .when()
                .get("?operator={value}", oper)
                .then().log().all()
                .statusCode(200)
                .extract().body().jsonPath().getList("",ResultData.class);
        if(oper.equals("%3D")){
            oper = "=";
        }
        for (ResultData entry : entries) {
            Assertions.assertEquals(entry.getOperator(), oper);
        }
    }
    @Test
    @DisplayName("GET operators негативный")
    @Description("GET запрос с параметром operators и неподдерживаемым оператором. Возвращает ошибку")
    @Tag("Негативные")
    @Tag("GET")
    public void OperatorNegative(){
        given()
                .spec(Specifications.authCred())
                .when()
                .get("?operator=ggag")
                .then().log().all()
                .statusCode(400)
                .body("error", equalTo("not supported operator"));

    }

}
