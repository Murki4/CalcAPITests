package ApiTestsPackage;

import PojoClasses.PostRequestBody;
import PojoClasses.ResultData;
import SpecificationPackage.RequestResponceEvocation;
import SpecificationPackage.Specifications;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.*;
import io.restassured.internal.common.assertion.Assertion;
import io.restassured.response.Response;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Epic("Неклассифицированные тесты")
@DisplayName("Исследовательские/деструктивные тесты")
public class UnspecifiedTests {
    @BeforeAll
    static void InstallSpec(){ //стандартные спецификации
        Specifications.Install(Specifications.requestSpec());
    }
    @Test
    @Feature("Запросы к API")
    @DisplayName("Опрос разрешенных типов запроса")
    @Description("Пустой GET запрос для получения списка разрешенных запросов, сравнение со списком в документации. " +
            "При полном соответствии тест будет пройден, при провале будет выведен список ")
    @Tag("Позитивный")
    @Tag("GET")
    @Tag("Исследовательский")
    public void AllowedRequestList(){
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
    public void InvalidRequest(){
        given()
                .spec(Specifications.authCred())
                .when()
                .patch()
                .then()
                .log().all()
                .assertThat().statusCode(405);
    }
    @Test
    @Feature("Ограничения")
    @DisplayName("Перенаполнение операторов number")
    @Description("POST запрос с оператором '+' и двумя числами с плавающей точкой, " +
            "где после точки неограниченное количество цифр. Должен вернуть ошибку и код 400.")
    @Tag("POST")
    @Tag("Негативные")
    @Tag("Исследовательские")
    void PostMultiDoubleFull() {
        RequestResponceEvocation.Evok400(new PostRequestBody("+", true));
    }
    @Test
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
    @Disabled
    @Feature("Безопасность")
    @Description("Выборочная проверка id до 100. Любые вхождения будут записаны в файл отчета")
    //а что если... id общие для всех и чужие данные доступны?
    public void CheckId() {
        int i = 0;
        String list = "";
        Response response;
        while (i < 100){
            response = given()
                    .spec(Specifications.authCred())
                    .when()
                    .get(i + "/")
                    .then().log().all()
                    .extract().response().as(Response.class);
            if(response.getStatusCode()!=404){
                try {
                    list += new ObjectMapper().writeValueAsString(response.getBody().as(ResultData.class));
                }
                catch (Exception e){
                    list += "Не удалось записать строку\n";
                }
            }
            i++;
        }
        if(list.isEmpty()) {
            Assertions.fail("Чужих данных нет");
        }
        else{
            Allure.addAttachment("Список чужих данных", list);
            Assertions.fail("Найдены чужие данные");
        }
    }
    //могу ли я их удалить?
    @Test
    @Disabled
    @Feature("Безопасность")
    @DisplayName("Удаление чужой записи")
    @Description("Поиск записи не принадлежащей sys0b1_5 и попытка ее удалить")
    public void DeleteEnemy(){
        Response response;
        int i = 56;
        do{
            i++;
            response = given()
                    .spec(Specifications.authCred())
                    .when()
                    .get(i+"/")
                    .then().log().all()
                    .extract().response();
        }while(i<1000&&!(response.getStatusCode()==200&&response.getBody().path("user")!="sys0b1_5"));
        if(i==999){
            Assertions.fail("Не удалось найти запись другого пользователя");
        }
        else{
            given()
                    .spec(Specifications.authCred())
                    .when()
                    .delete(i+"/")
                    .then().log().all()
                    .assertThat().statusCode(200)
                    .body("message", IsEqual.equalTo("operation "+ i + " is deleted"));
            given()
                    .spec(Specifications.authCred())
                    .when()
                    .get(i+"/")
                    .then().log().all()
                    .assertThat().statusCode(404)
                    .body("error", IsEqual.equalTo("id not found or incorrect data"));
        } //ой-ёй
    }
}

