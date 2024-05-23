package ApiTestsPackage;

import PojoClasses.PostRequestBody;
import PojoClasses.ResultData;
import SpecificationPackage.Specifications;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import static org.hamcrest.core.IsEqual.equalTo;

import static io.restassured.RestAssured.given;

@Epic("Operations with entries")
public class IdTest {
    private static Integer idlist;
    @BeforeAll
    static void InstallSpecAndMakeTestEntry(){ //установка стандартных спецификаций и создание набора данных для теста
        Specifications.Install(Specifications.requestSpec());
        IdTest.idlist = given()
                .spec(Specifications.authCred())
                .body(new PostRequestBody("+"))
                .when()
                .post()
                .then().log().all()
                .extract().path("pk");
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
    }
    @Test
    @DisplayName("GET Id")
    @Description("GET запрос с параметром 'id', где id это номер не существующей записи в БД. " +
            "Возращает код 404 и ошибку 'id not found or incorrect data'")
    @Tag("GET")
    @Tag("Негативный")
    public void GetIdNegative(){
        given()
                .spec(Specifications.authCred())
                .when()
                .get("1")
                .then().log().all()
                .assertThat().statusCode(404)
                .body("error", equalTo("id not found or incorrect data"));

    }
}