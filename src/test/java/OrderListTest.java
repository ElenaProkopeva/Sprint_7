import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
// дополнительный статический импорт нужен, чтобы использовать given(), get() и then()
import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.*;

public class OrderListTest extends BaseApi{

    @Before
    public void setUp() {
        super.setupRequestSpecification();
    }

    @Test
    @DisplayName("Проверка получения списка заказов")
    @Description("Успешное получение списка заказов")
    public void checkOrderList(){
        requestSpecification
                .given()
                .get(ORDERS_ENDPOINT) // отправь запрос на ручку
                .then().assertThat().body("orders", notNullValue())
                .and()
                .statusCode(SC_OK);
    }
}
