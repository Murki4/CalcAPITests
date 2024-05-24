package ApiTestsPackage;

import PojoClasses.PostRequestBody;
import PojoClasses.ResultData;
import SpecificationPackage.RequestResponceEvocation;
import SpecificationPackage.Specifications;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import org.junit.jupiter.api.*;

import static org.hamcrest.core.IsEqual.equalTo;
import static io.restassured.RestAssured.given;

@Epic("Операции с id")
@DisplayName("Операции с id")
public class IdTest {
    private static Integer idlist;
    @BeforeEach
    void InstallSpecAndMakeTestEntry(){ //установка стандартных спецификаций и создание набора данных для теста
        Specifications.Install(Specifications.requestSpec());
        IdTest.idlist = given()
                .spec(Specifications.authCred())
                .body(new PostRequestBody("+"))
                .when()
                .post()
                .then().log().all()
                .extract().path("pk");
    }

    @AfterEach
    void DeleteEntries() { //удаление тестовых данных из БД;
        RequestResponceEvocation.EvokeDeletion();
    }

    @Test
    @Feature("Позитивный")
    @DisplayName("GET Id")
    @Description("GET запрос с параметром 'id', где id это номер существующей записи в БД. " +
            "Возращает информацию о записи")
    @Tag("GET")
    @Tag("Позитивный")
    public void GetId(){
        ResultData result = given()
                .spec(Specifications.authCred())
                .when()
                .get(Integer.toString(IdTest.idlist))
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().body().as(ResultData.class);
        Assertions.assertEquals(IdTest.idlist, result.getPk());
        Assertions.assertNotNull(result.getNumber_1());
        Assertions.assertNotNull(result.getNumber_2());
        Assertions.assertNotNull(result.getOperator());
        Assertions.assertNotNull(result.getResult());
        Assertions.assertNotNull(result.getUser());
        Assertions.assertNotNull(result.getDate_create());
    }

    @Test
    @Flaky
    @Feature("Негативный")
    @DisplayName("GET Id")
    @Description("GET запрос с параметром 'id', где id это номер не существующей записи в БД. " +
            "Возращает код 404 и ошибку 'id not found or incorrect data'")
    @Tag("GET")
    @Tag("Негативный")
    public void GetIdNegative(){
        given()
                .spec(Specifications.authCred())
                .when()
                .get(IdTest.idlist + 10 + "/")
                .then().log().all()
                .assertThat().statusCode(404)
                .body("error", equalTo("id not found or incorrect data"));

    }
    @Test
    @Feature("Позитивный")
    @DisplayName("DELETE Id")
    @Description("DELETE запрос с параметром 'id', где id это номер существующей записи в БД. " +
            "Должен вернуть код 200 и сообщение об удалении")
    @Tag("GET")
    @Tag("Позитивный")
    public void DeleteId(){
        given()
                .spec(Specifications.authCred())
                .when()
                .delete(Integer.toString(IdTest.idlist))
                .then().log().all()
                .assertThat().statusCode(200)
                .body("message",equalTo("operation "+ IdTest.idlist + " is deleted"));
        given()
                .spec(Specifications.authCred())
                .when()
                .get(IdTest.idlist+"/")
                .then().log().all()
                .assertThat().statusCode(404)
                .body("error", equalTo("id not found or incorrect data"));
    }

    @Test
    @Feature("Негативный")
    @DisplayName("DELETE Id")
    @Description("DELETE запрос с параметром 'id', где id это номер не существующей записи в БД. " +
            "Должен вернуть код 404 и ошибку 'id not found or incorrect data'")
    @Tag("DELETE")
    @Tag("Негативный")
    public void DeleteIdNegative(){
        given()
                .spec(Specifications.authCred())
                .when()
                .delete("1/")
                .then().log().all()
                .assertThat().statusCode(404)
                .body("error", equalTo("id not found or incorrect data"));
    }
}