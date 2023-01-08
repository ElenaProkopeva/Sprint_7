import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
// дополнительный статический импорт нужен, чтобы использовать given(), get() и then()
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;

    public OrderCreateTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] orderData(){
        return new Object[][] {
                { "Елена", "Фам", "Моска 12", "Чистые пруды", "79501234545", 2, "2020-06-06", "comment", new String[]{"BLACK"}},
                { "Елена", "Фам", "Моска 12", "Чистые пруды", "79501234545", 2, "2020-06-06", "comment", new String[]{"GREY"}},
                { "Елена", "Фам", "Моска 12", "Чистые пруды", "79501234545", 2, "2020-06-06", "comment", new String[]{"BLACK", "GREY"}},
                { "Елена", "Фам", "Моска 12", "Чистые пруды", "79501234545", 2, "2020-06-06", "comment", new String[]{}},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void checkOrderCreate(){
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate,comment, color);
        Response response =
                given()
                        .header("Content-type", "application/json")// заполни header
                        .body(order) // заполни body
                        .when()
                        .post("/api/v1/orders"); // отправь запрос на ручку
        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);
    }
}
