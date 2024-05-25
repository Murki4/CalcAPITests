package ApiTestsPackage;

import PojoClasses.ResultData;
import SpecificationPackage.Specifications;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@Epic("Безопасность")
@DisplayName("Тесты безопасности")
public class SecurityTests {

    @BeforeAll
    static void InstallSpec(){ //стандартные спецификации
        Specifications.Install(Specifications.requestSpec());
    }

    @Test
    @Flaky
    @Disabled
    @Feature("Доступ к данным")
    @DisplayName("Перебор id")
    @Description("Выборочная проверка id от 0 до 1000. Любые вхождения будут записаны в файл отчета")
    @Tag("GET")
        //а что если... id общие для всех и чужие данные доступны?
    void CheckAllId() {
        int i = 0;
        String list = "";
        while (i < 1000){
            Response response = given()
                    .spec(Specifications.authCred())
                    .noFilters()
                    .when()
                    .get(i + "/")
                    .then().log().all()
                    .extract().response();
            if(response.getStatusCode()==200&response.getBody().path("user")!="sys0b1_5"){
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
            Allure.addAttachment("message","Чужих данных нет");
        }
        else{
            Allure.addAttachment("Список чужих данных", list);
            Assertions.fail("Найдены чужие данные");
        }
    }
    //могу ли я их удалить?
    @Test
    @Flaky
    @Disabled
    @Feature("Доступ к данным")
    @DisplayName("Удаление чужой записи")
    @Description("Поиск записи не принадлежащей sys0b1_5 и попытка ее удалить")
    @Tag("DELETE")
    void DeleteNotOwnedEntryRandomCheck(){
        Response response;
        int i = 100;
        do{
            i++;
            response = given()
                    .spec(Specifications.authCred())
                    .when()
                    .get(i+"/")
                    .then().log().all()
                    .extract().response();
        }while(i<1000&&!(response.getStatusCode()==200&&response.getBody().path("user")!="sys0b1_5"));
        if(i==1000){
            Allure.addAttachment("message","Не удалось найти запись другого пользователя");
        }
        else{
            given() //попытка удаления записи
                    .spec(Specifications.authCred())
                    .when()
                    .delete(i+"/")
                    .then().log().all()
                    .assertThat().statusCode(200)
                    .body("message", IsEqual.equalTo("operation "+ i + " is deleted"));
            given() //проверка отсутствия записи по id
                    .spec(Specifications.authCred())
                    .when()
                    .get(i+"/")
                    .then().log().all()
                    .assertThat().statusCode(404)
                    .body("error", IsEqual.equalTo("id not found or incorrect data"));
            Assertions.fail("Запись другого пользователя успешно удалена"); //ой-ёй
        }
    }
    @Test
    @Flaky
    @Disabled
    @Feature("Доступ к данным")
    @DisplayName("Удаление чужой записи")
    @Description("Удаление уже известной записи не принадлежащей sys0b1_5")
    @Tag("DELETE")
    void DeleteNotOwnedEntry(){
        given() //попытка удаления записи
                .spec(Specifications.authCred())
                .when()
                .delete("76/")
                .then().log().all()
                .assertThat().statusCode(200)
                .body("message", IsEqual.equalTo("operation 76 is deleted"));
        given() //проверка отсутствия записи по id
                .spec(Specifications.authCred())
                .when()
                .get("76/")
                .then().log().all()
                .assertThat().statusCode(404)
                .body("error", IsEqual.equalTo("id not found or incorrect data"));
        Assertions.fail("Запись другого пользователя успешно удалена");
    }
}
