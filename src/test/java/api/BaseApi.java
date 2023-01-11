import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;

public class BaseApi {

    RequestSpecification requestSpecification;
    protected final static String COURIER_CREATE_ENDPOINT = "/courier";
    protected final static String COURIER_LOGIN_ENDPOINT = "/courier/login";
    protected final static String ORDERS_ENDPOINT = "/orders";

    @Before
    public void setupRequestSpecification()
    {
        requestSpecification = RestAssured.given()
                .baseUri("http://qa-scooter.praktikum-services.ru/")
                .basePath("/api/v1")
                .header("Content-type", "application/json");
    }
}
