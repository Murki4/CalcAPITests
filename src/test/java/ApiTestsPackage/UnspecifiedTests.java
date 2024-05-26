package ApiTestsPackage;

import PojoClasses.PostRequestBody;
import PojoClasses.ResultData;
import SpecificationPackage.RequestResponceEvocation;
import SpecificationPackage.Specifications;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Epic("Неклассифицированные тесты")
@DisplayName("Исследовательские/деструктивные тесты")
public class UnspecifiedTests {

    @BeforeAll
    static void InstallSpec(){ //стандартные спецификации
        Specifications.Install(Specifications.requestSpec());
    }

    @AfterAll
    static void DeleteEntries(){
        RequestResponceEvocation.EvokeDeletion();
    }

    @Test
    @Disabled
    @Feature("Ограничения")
    @DisplayName("Опрос разрешенных типов запроса")
    @Description("Пустой GET запрос для получения списка разрешенных запросов, сравнение со списком в документации. " +
            "При полном соответствии тест будет пройден, при провале будет выведен список ")
    @Tag("Позитивный")
    @Tag("GET")
    @Tag("Исследовательский")
    void AllowedRequestList(){
        String allow = given()
                .spec(Specifications.authCred())
                .when()
                .get()
                .then()
                .log().all()
                .extract().header("Allow");
        String[] doc_requests = {"POST", "GET", "DELETE", "OPTIONS"};
        String[] allow_requests = allow.split(", ");
        ArrayList<String> mismatch_requests = new ArrayList<>();
        for(String allow_requests_member: allow_requests){
            if(!Arrays.asList(doc_requests).contains(allow_requests_member)){
                mismatch_requests.add(allow_requests_member);
            }
        }
        Assertions.assertEquals(new ArrayList<>(),mismatch_requests);
    }

    @Test
    @Feature("Ограничения")
    @DisplayName("Некорректный запрос")
    @Description("PATCH запрос для просмотра отрабатывания запросов не принимаемых API. " +
            "Должен вернуть код 405")
    @Tag("Негативный")
    @Tag("Исследовательский")
    void InvalidRequest(){
        given()
                .spec(Specifications.authCred())
                .when()
                .patch()
                .then()
                .log().all()
                .assertThat().statusCode(405);
    }

    @Test
    @Feature("Функционал")
    @DisplayName("Удаление всех записей")
    @Description("DELETE запрос без операторов. Удаляет все записи данног опользователя из бд. " +
            "Возвращает код 200 и message = all operations of the curent user gave been deleted")
    void DeleteAll(){
        given()//создание тестовой записи
                .spec(Specifications.authCred())
                .body(new PostRequestBody("+"))
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201);
        given()//удаление всех записей
                .spec(Specifications.authCred())
                .when()
                .delete()
                .then()
                .log().all()
                .assertThat().statusCode(200)
                .body("message",equalTo("all operations of the current user have been deleted"));
        List<ResultData> entries = given() //проверка что действительно ничего нет
                .spec(Specifications.authCred())
                .when()
                .get()
                .then().log().all()
                .statusCode(200)
                .extract().body().jsonPath().getList("",ResultData.class);
        Assertions.assertEquals(new ArrayList<>(),entries);
    }

    @Test
    @Disabled
    @Feature("Функции")
    @DisplayName("OPTIONS")
    @Description("OPTIONS запрос для получения спецификации по REST API")
    @Tag("OPTIONS")
    @Tag("Исследовательский")
    void GetOptions(){
        String l = given()
                .spec(Specifications.authCred())
                .when()
                .options()
                .then()
                .log().all()
                .assertThat().statusCode(200)
                .extract().body().jsonPath().get("").toString();
        Allure.addAttachment("Options", l);
    }

    @Test
    @Disabled
    @Feature("Ограничения")
    @DisplayName("Количество поддерживаемых символов в опреаторах number")
    @Description("POST запрос с оператором '+' и двумя числами, " +
            "количество знаков в числе увеличивается от 1 до n пока не выдаст код 400. " +
            "Возвращает fail и предельное количество знаков в числе.")
    @Tag("POST")
    @Tag("Исследовательские")
    void HowManyNumbersSupported() {
        String num1 = "";
        String num2 = "1";
        int response;
        do{
            num1+= "1";
            PostRequestBody body = new PostRequestBody(num1, num2, "+");
            response = given()
                    .spec(Specifications.authCred())
                    .body(body)
                    .when()
                    .post()
                    .then().log().all()
                    .extract().statusCode();
        }while(response!=400);
        Assertions.fail(num1.length()-1 + " максимально разрешенное кол-во знаков в числе");
    }

    @Test
    @Feature("Ограничения")
    @DisplayName("POST Неверный оператор для действий с числами")
    @Description("POST запрос с оператором '(' и двумя числами. Должен вернуть код 201 и параметр result = error")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostWrongOperator() {
        RequestResponceEvocation.Evoke201Negative(new PostRequestBody("("),"error");
    }
}

