import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.*;

public class CourierLoginTest extends CourierApi{

    @Before
    public void setUp() {
        super.setupRequestSpecification();
    }

    //курьер может авторизоваться;
    //успешный запрос возвращает id
    @Test
    @DisplayName("Проверка авторизации курьера (успешно)")
    @Description("Успешная проверка логина курьера")
    public void checkLoginCourier(){
        createCourier();
        loginCourier();
    }

    //для авторизации нужно передать все обязательные поля;
    //если какого-то поля нет, запрос возвращает ошибку;
    @Test
    @DisplayName("Проверка авторизации курьера без пароля")
    @Description("Проверка, что нельзя залогиниться без ввода пароля")
    public void checkLoginCourierWithoutPassword(){
        String json = "{\"login\": \"ya\", \"password\": \"\"}";
        requestSpecification
                .given()
                .body(json) // заполни body
                .when()
                .post(COURIER_LOGIN_ENDPOINT) // отправь запрос на ручку
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
        String json = "{\"password\": \"1234\"}";
        requestSpecification
                .given()
                .body(json) // заполни body
                .when()
                .post(COURIER_LOGIN_ENDPOINT) // отправь запрос на ручку
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
        String json = "{\"login\": \"ya\", \"password\": \"1234\"}";
        requestSpecification
                .given()
                .body(json) // заполни body
                .when()
                .post(COURIER_LOGIN_ENDPOINT) // отправь запрос на ручку
                .then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(SC_NOT_FOUND);
    }

    @After
    public void dataClean(){
        deleteCourier();
    }
}
