package ApiTestsPackage;

import SpecificationPackage.Specifications;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

@Epic("Авторизация")

public class AuthorizationTests {

    @Test
    @DisplayName("Авторизация положительный")
    @Description("GET запрос с валидной парой логин_пароль. Использует отправку до запроса данных сервера. " +
            "Возвращает код ответа 200")
    @Tag("Положительные")
    @Tag("GET")
    public void AuthorizationPositive(){
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(200));
            given()
                .spec(Specifications.authCred())
                .when()
                .get()
                .then()
                .log().all();
    }
    @Test
    @DisplayName("Авторизация отрицательный. Неправильные данные")
    @Description("GET запрос с несуществующей парой логин_пароль. Возвращает код 403 и сообщение Invalid username/password.")
    @Tag("Негативные")
    @Tag("GET")
    public void AuthorizationNegativeWrongCredentials(){
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(403));
                given()
                        .auth().preemptive().basic("default", "default")
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .assertThat().body("detail", equalTo("Invalid username/password."));
    }
    @Test
    @DisplayName("Авторизация отрицательный. Отсутствуют данные")
    @Description("GET запрос без пары логин_пароль. Возвращает код 403 и сообщение Authentication credentials were not provided.")
    @Tag("Негативные")
    @Tag("GET")
    public void AuthorizationNegativeEmptyCredentials(){
        Specifications.Install(Specifications.requestSpec(), Specifications.responseSpec(403));
        given()
                .when()
                .get()
                .then()
                .log().all()
                .assertThat().body("detail", equalTo("Authentication credentials were not provided."));
    }

}
