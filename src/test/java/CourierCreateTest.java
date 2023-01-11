import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.*;

public class CourierCreateTest extends CourierApi{

    @Before
    public void setUp() {
        super.setupRequestSpecification();
    }

    //курьера можно создать;
    //запрос возвращает правильный код ответа;
    //успешный запрос возвращает ok: true
    @Test
    @DisplayName("Проверка создания курьера (успешно)")
    @Description("Успешная проверка создания курьера")
    public void checkCreateCourier(){
        createCourier();
    }

    //нельзя создать двух одинаковых курьеров;
    //если создать пользователя с логином, который уже есть, возвращается ошибка.
    @Test
    @DisplayName("Проверка создания курьера с дублирующими данными")
    @Description("Проверка, что нельзя создать дубль курьера")
    public void checkCreateDuplicateCourier(){
        Courier courier = createCourier();
        requestSpecification
                .given()
                .body(courier) // заполни body
                .when()
                .post(COURIER_CREATE_ENDPOINT) // отправь запрос на ручку
                .then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(SC_CONFLICT);
    }

    //чтобы создать курьера, нужно передать в ручку все обязательные поля
    //если одного из полей нет, запрос возвращает ошибку;
    @Test
    @DisplayName("Проверка создания курьера без пароля и имени")
    @Description("Проверка, что нельзя создать курьера без пароля и имени")
    public void checkCreateCourierWithoutPassName(){
        Courier courier = new Courier("ya", "", "");
        requestSpecification
                .given()
                .body(courier) // заполни body
                .when()
                .post(COURIER_CREATE_ENDPOINT) // отправь запрос на ручку
                .then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    //чтобы создать курьера, нужно передать в ручку все обязательные поля
    //если одного из полей нет, запрос возвращает ошибку;
    @Test
    @DisplayName("Проверка создания курьера без логина и имени")
    @Description("Проверка, что нельзя создать курьера без логина и имени")
    public void checkCreateCourierWithoutLoginName(){
        Courier courier = new Courier("", "1234", "");
        requestSpecification
                .given()
                .body(courier) // заполни body
                .when()
                .post(COURIER_CREATE_ENDPOINT) // отправь запрос на ручку
                .then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    //чтобы создать курьера, нужно передать в ручку все обязательные поля
    //если одного из полей нет, запрос возвращает ошибку;
    @Test
    @DisplayName("Проверка создания курьера без пароля и логина")
    @Description("Проверка, что нельзя создать курьера без пароля и логина")
    public void checkCreateCourierWithoutLoginPass(){
        Courier courier = new Courier("", "", "saske");
        requestSpecification
                .given()
                .body(courier) // заполни body
                .when()
                .post(COURIER_CREATE_ENDPOINT) // отправь запрос на ручку
                .then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @After
    public void dataClean(){
        deleteCourier();
    }
}
