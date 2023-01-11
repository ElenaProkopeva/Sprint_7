import api.BaseApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.Order;
import api.OrderApi;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest extends BaseApi {

    private final Order order;
    private OrderApi orderApi;

    public OrderCreateTest(Order order) {
        this.order = order;
    }

    @Parameterized.Parameters
    public static Object[][] orderData(){
        return new Object[][] {
                { new Order("Елена", "Фам", "Моска 12", "Чистые пруды", "79501234545", 2, "2020-06-06", "comment", new String[]{"BLACK"})},
                { new Order("Елена", "Фам", "Моска 12", "Чистые пруды", "79501234545", 2, "2020-06-06", "comment", new String[]{"GREY"})},
                { new Order("Елена", "Фам", "Моска 12", "Чистые пруды", "79501234545", 2, "2020-06-06", "comment", new String[]{"BLACK", "GREY"})},
                { new Order("Елена", "Фам", "Моска 12", "Чистые пруды", "79501234545", 2, "2020-06-06", "comment", new String[]{})},
        };
    }

    @Before
    public void setUp() {
        super.setupRequestSpecification();
        this.orderApi = new OrderApi(requestSpecification);
    }

    @Test
    @DisplayName("Проверка создания заказов")
    @Description("Проверка создания заказов с разными color")
    public void checkOrderCreate(){
        OrderApi orderApi = new OrderApi(requestSpecification);
        orderApi.setOrder(order);
        orderApi.createOrder()
                .then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(SC_CREATED);
    }
}
