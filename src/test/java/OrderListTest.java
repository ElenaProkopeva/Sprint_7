import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
// дополнительный статический импорт нужен, чтобы использовать given(), get() и then()
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class OrderListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void checkOrderList(){
        Response response =
                given()
                        .header("Content-type", "application/json")// заполни header
                        .get("/api/v1/orders"); // отправь запрос на ручку
        response.then().assertThat().body("orders", notNullValue())
                .and()
                .statusCode(200);
    }
}
