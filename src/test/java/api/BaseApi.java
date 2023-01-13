package api;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;

public class BaseApi {

    protected RequestSpecification requestSpecification;

    @Before
    public void setupRequestSpecification()
    {
        requestSpecification = RestAssured.given()
                .baseUri("http://qa-scooter.praktikum-services.ru/")
                .basePath("/api/v1")
                .header("Content-type", "application/json");
    }
}
