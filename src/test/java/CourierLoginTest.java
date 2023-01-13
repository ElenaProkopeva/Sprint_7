import api.BaseApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import api.CourierApi;
import org.example.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.*;

public class CourierLoginTest extends BaseApi {

    private CourierApi courierApi;

    @Before
    public void setUp() {
        super.setupRequestSpecification();
        this.courierApi = new CourierApi(requestSpecification);
    }

    //курьер может авторизоваться;
    //успешный запрос возвращает id
    @Test
    @DisplayName("Проверка авторизации курьера (успешно)")
    @Description("Успешная проверка логина курьера")
    public void checkLoginCourier(){
        courierApi.setCourier(new Courier("ya", "1234", "saske"));
        courierApi.createCourier();
        courierApi.loginCourier()
                .then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

    //для авторизации нужно передать все обязательные поля;
    //если какого-то поля нет, запрос возвращает ошибку;
    @Test
    @DisplayName("Проверка авторизации курьера без пароля")
    @Description("Проверка, что нельзя залогиниться без ввода пароля")
    public void checkLoginCourierWithoutPassword(){
        courierApi.setCourier(new Courier("ya", ""));
        courierApi.loginCourier()
                .then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    //для авторизации нужно передать все обязательные поля;
    //если какого-то поля нет, запрос возвращает ошибку;
    @Test
    @DisplayName("Проверка авторизации курьера без логина")
    @Description("Проверка, что нельзя залогиниться без ввода логина")
    public void checkLoginCourierWithoutLogin(){
        courierApi.setCourier(new Courier("", "1234"));
        courierApi.loginCourier()
                .then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    //система вернёт ошибку, если неправильно указать логин или пароль;
    //если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
    @Test
    @DisplayName("Проверка авторизации курьера с некорректным логином и паролем")
    @Description("Проверка, что нельзя залогиниться с некорректным логином и паролем")
    public void checkLoginPasswordCourierIsIncorrect(){
        courierApi.setCourier(new Courier("null", "null"));
        courierApi.loginCourier()
                .then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(SC_NOT_FOUND);
    }

    @After
    public void dataClean(){
        courierApi.deleteCourier();
    }
}
