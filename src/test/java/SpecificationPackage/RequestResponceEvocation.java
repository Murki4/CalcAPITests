package SpecificationPackage;

import PojoClasses.ResultData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RequestResponceEvocation {
    public static ResultData Evok201(Object body){
        return given()
                .spec(Specifications.authCred())
                .body(body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
    }
    public static void Evok201Negative(Object body){ //Нужен для потенциального клиента API и возвращения результата операции как error и записи в историю
        given()
                .spec(Specifications.authCred())
                .body(body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .body("result", equalTo("error"));
    }
    public static void Evok201Negative(Object body, String result){ //Если параметр result имеет отличное от стандартного значение
        given()
                .spec(Specifications.authCred())
                .body(body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .body("result", equalTo(result));
    }
    public static void Evok400(Object body){
        given()
                .spec(Specifications.authCred())
                .body(body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(400)
                .body("error", equalTo("incorrect data"));
    }
    public static void EvokDeletion(){
        given()
                .spec(Specifications.authCred())
                .when()
                .delete()
                .then()
                .log().all();
    }
}
