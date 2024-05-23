package ApiTestsPackage;

import SpecificationPackage.Specifications;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
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
    @DisplayName("Опрос разрешенных типов запроса")
    @Description("Пустой GET запрос для получения списка разрешенных запросов, сравнение со списком в документации. " +
            "При полном соответствии тест будет пройден")
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
        String[] doc_requests = {"POST", "GET", "DELETE" };
        String[] allow_requests = allow.split(", ");
        ArrayList<String> mismatch_requests = new ArrayList<>();
        for(String allow_requests_member: allow_requests){
            if(!Arrays.asList(doc_requests).contains(allow_requests_member)){
                mismatch_requests.add(allow_requests_member);
            }
        }
        Assertions.assertNull(mismatch_requests);
    }
    @Test
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

}

