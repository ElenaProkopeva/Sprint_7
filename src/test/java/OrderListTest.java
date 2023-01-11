import api.BaseApi;
import api.OrderApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
// дополнительный статический импорт нужен, чтобы использовать given(), get() и then()
import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.*;

public class OrderListTest extends BaseApi {

    private OrderApi orderApi;

    @Before
    public void setUp() {
        super.setupRequestSpecification();
        this.orderApi = new OrderApi(requestSpecification);
    }

    @Test
    @DisplayName("Проверка получения списка заказов")
    @Description("Успешное получение списка заказов")
    public void checkOrderList(){
        orderApi.getOrderList()
                .then().assertThat().body("orders", notNullValue())
                .and()
                .statusCode(SC_OK);
    }
}
