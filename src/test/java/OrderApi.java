import io.qameta.allure.Step;
import org.example.Order;
import static org.hamcrest.Matchers.notNullValue;
import static org.apache.http.HttpStatus.*;

public class OrderApi extends BaseApi{

    private final Order order;

    public OrderApi(Order order) {
        this.order = order;
    }

    @Step("Создать заказ и получить track")
    public void createOrder(){
        requestSpecification
                .given()
                .body(order) // заполни body
                .when()
                .post(ORDERS_ENDPOINT) // отправь запрос на ручку
                .then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(SC_CREATED);
    }

}
