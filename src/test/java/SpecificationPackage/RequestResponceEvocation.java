package SpecificationPackage;

import PojoClasses.ResultData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RequestResponceEvocation {
    public static ResultData Evoke201(Object body){
        return given()
                .spec(Specifications.authCred())
                .body(body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
    }
    //Нужен для потенциального клиента API и сохранения истории операций. Несмотря на некорректную операцию записывает ее в БД
    public static void Evoke201Negative(Object body){
        given()
                .spec(Specifications.authCred())
                .body(body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .body("result", equalTo("error"));
    }

    //Если параметр result имеет отличное от стандартного значение
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
    public static void Evoke400(Object body){
        given()
                .spec(Specifications.authCred())
                .body(body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(400)
                .body("error", equalTo("incorrect data"));
    }
    public static void EvokeDeletion(){
        given()
                .spec(Specifications.authCred())
                .when()
                .delete()
                .then()
                .log().all();
    }
}
