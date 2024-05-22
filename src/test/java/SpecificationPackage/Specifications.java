package SpecificationPackage;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;


public class Specifications{
    private static final String uri = "https://calc.sintetika.keenetic.pro/api/";

    public static RequestSpecification requestSpec(){
        return new RequestSpecBuilder()
                .setBaseUri(uri)
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }

    public static void Install(RequestSpecification request){
        RestAssured.requestSpecification = request;
    }
    public static RequestSpecification authCred(){
        return RestAssured.given().auth().preemptive().basic("sys0b1_5", "ymJ-q7z-7N2-SZz");
    }

}
