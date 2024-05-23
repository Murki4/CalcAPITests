package SpecificationPackage;

import PojoClasses.ResultData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RequestResponceEvocation<T> {
    public ResultData Evoc201(T body){
        return given()
                .spec(Specifications.authCred())
                .body(body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().body().as(ResultData.class);
    }
    public void Evoc400(T body){
        given()
                .spec(Specifications.authCred())
                .body(body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(400)
                .body("error", equalTo("incorrect data"));
    }
}
