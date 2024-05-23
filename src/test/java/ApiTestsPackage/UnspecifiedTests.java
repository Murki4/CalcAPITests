package ApiTestsPackage;

import PojoClasses.PostRequestBody;
import SpecificationPackage.RequestResponceEvocation;
import SpecificationPackage.Specifications;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.given;

@Epic("Неклассифицированные тесты")
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

}

