package ApiTestsPackage;

import SpecificationPackage.Specifications;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

@Epic("Авторизация")
@DisplayName("Тесты авторизации в API")
public class AuthorizationTests {
    @BeforeAll
    static void InstallSpec(){ //стандартные спецификации
        Specifications.Install(Specifications.requestSpec());
    }

    @Test
    @Feature("Позитивный")
    @DisplayName("Авторизация позитивный")
    @Description("GET запрос с валидной парой логин_пароль. Использует отправку до запроса данных сервера. " +
            "Возвращает код ответа 200")
    @Tag("Порзитивный")
    @Tag("GET")
    public void AuthorizationPositive(){
        given()
                .spec(Specifications.authCred())
                .when()
                .get()
                .then()
                .log().all()
                .assertThat().statusCode(200);
    }
    @Test
    @Feature("Негативный")
    @DisplayName("Авторизация негативный. Неправильные данные")
    @Description("GET запрос с несуществующей парой логин_пароль. Возвращает код 403 и сообщение Invalid username/password.")
    @Tag("Негативные")
    @Tag("GET")
    public void AuthorizationNegativeWrongCredentials(){
                given()
                        .auth().preemptive().basic("default", "default")
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .assertThat().statusCode(403)
                        .assertThat().body("detail", equalTo("Invalid username/password."));
    }
    @Test
    @Feature("Негативный")
    @DisplayName("Авторизация негативный. Отсутствуют данные")
    @Description("GET запрос без пары логин_пароль. Возвращает код 403 и сообщение Authentication credentials were not provided.")
    @Tag("Негативные")
    @Tag("GET")
    public void AuthorizationNegativeEmptyCredentials(){
        given()
                .when()
                .get()
                .then()
                .log().all()
                .assertThat().statusCode(403)
                .assertThat().body("detail", equalTo("Authentication credentials were not provided."));
    }

}
