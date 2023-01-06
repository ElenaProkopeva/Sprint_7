import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Courier;
import org.junit.Before;
import org.junit.Test;
// дополнительный статический импорт нужен, чтобы использовать given(), get() и then()
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CourierLoginTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    //курьер может авторизоваться;
    //успешный запрос возвращает id
    @Test
    public void checkLoginCourier(){
        Courier courier = new Courier("ya", "1234", "saske");
                given()
                        .header("Content-type", "application/json")// заполни header
                        .body(courier) // заполни body
                        .when()
                        .post("/api/v1/courier") // отправь запрос на ручку
                        .then().statusCode(201);
        Response response =
                given()
                        .header("Content-type", "application/json")// заполни header
                        .body(courier) // заполни body
                        .when()
                        .post("/api/v1/courier/login"); // отправь запрос на ручку
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
        deleteCourier(courier);
    }

    //для авторизации нужно передать все обязательные поля;
    //если какого-то поля нет, запрос возвращает ошибку;
    @Test
    public void checkLoginCourierWithoutPassword(){
        String json = "{\"login\": \"ya\", \"password\": \"\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")// заполни header
                        .body(json) // заполни body
                        .when()
                        .post("/api/v1/courier/login"); // отправь запрос на ручку
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    //для авторизации нужно передать все обязательные поля;
    //если какого-то поля нет, запрос возвращает ошибку;
    @Test
    public void checkLoginCourierWithoutLogin(){
        String json = "{\"password\": \"1234\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")// заполни header
                        .body(json) // заполни body
                        .when()
                        .post("/api/v1/courier/login"); // отправь запрос на ручку
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    //система вернёт ошибку, если неправильно указать логин или пароль;
    //если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
    @Test
    public void checkLoginPasswordCourierIsIncorrect(){
        String json = "{\"login\": \"ya\", \"password\": \"1234\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")// заполни header
                        .body(json) // заполни body
                        .when()
                        .post("/api/v1/courier/login"); // отправь запрос на ручку
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    public void deleteCourier(Courier courier){
        Integer courierId = given()
                .header("Content-type", "application/json")// заполни header
                .body(courier) // заполни body
                .when()
                .post("/api/v1/courier/login") // отправь запрос на ручку
                .then().extract().body().path("id");
        given()
                .delete("/api/v1/courier/{id}", courierId.toString()) // отправка DELETE-запроса
                .then().assertThat().statusCode(200); // проверка, что сервер вернул код 200
    }
}
