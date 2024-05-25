package SpecificationPackage;

import PojoClasses.ResultData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RequestResponceEvocation {
    public static ResultData Evoke201(Object body){ //201 ответ возвращающий результат вычислений
        return given()
                .spec(Specifications.authCred())
                .body(body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
    }

    //Ответ 201 с ошибкой. В теории подойдет для вывода ошибки в клиенте API и дополнительных записей в истории о неудачных операциях
    //Условие: number_1 и number_2 должны быть не более 4 символов
    //Запишу как «Не баг а ‘фича’»
    public static void Evoke201Negative(Object body, String result){
        given()
                .spec(Specifications.authCred())
                .body(body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .body("result", equalTo(result));
    }
    public static void Evoke400(Object body){ //400 ответ возвращающий error
        given()
                .spec(Specifications.authCred())
                .body(body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(400)
                .body("error", equalTo("incorrect data"));
    }
    public static void EvokeDeletion(){ //удаление всех записей из бд, использую для чистки тестовых данных после тестов
        given()
                .spec(Specifications.authCred())
                .when()
                .delete()
                .then()
                .log().all();
    }
}
