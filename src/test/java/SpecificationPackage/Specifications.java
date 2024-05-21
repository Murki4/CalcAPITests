package SpecificationPackage;

import PojoClasses.AuthorizationData;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specifications{
    public RequestSpecification requestSpecAuth(String url){
        AuthorizationData data = new AuthorizationData();
        return new RequestSpecBuilder()
                .setBaseUri(url)
                .setBody(data)
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }
    public RequestSpecification requestSpecAuth(String url, String username, String password){
        AuthorizationData data = new AuthorizationData(username, password);
        return new RequestSpecBuilder()
                .setBaseUri(url)
                .setBody(data)
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }
    public static ResponseSpecification responseSpec(int i){
        return new ResponseSpecBuilder()
                .log(LogDetail.valueOf("BODY"))
                .expectStatusCode(i)
                .build();
    }
    public static void Install(RequestSpecification request, ResponseSpecification response){
        RestAssured.requestSpecification = request;
        RestAssured.responseSpecification = response;
    }
}
