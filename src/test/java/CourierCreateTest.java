import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Courier;
import org.junit.Before;
import org.junit.Test;
// дополнительный статический импорт нужен, чтобы использовать given(), get() и then()
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CourierCreateTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    //курьера можно создать;
    //запрос возвращает правильный код ответа;
    //успешный запрос возвращает ok: true
    @Test
    public void checkCreateCourier(){
        Courier courier = new Courier("ya", "1234", "saske");
        Response response =
                given()
                        .header("Content-type", "application/json")// заполни header
                        .body(courier) // заполни body
                        .when()
                        .post("/api/v1/courier"); // отправь запрос на ручку
        response.then().assertThat().body("ok", is(true))
                .and()
                .statusCode(201);
        deleteCourier(courier);
    }

    //нельзя создать двух одинаковых курьеров;
    //если создать пользователя с логином, который уже есть, возвращается ошибка.
    @Test
    public void checkCreateDuplicateCourier(){
        Courier courier = new Courier("ya", "123", "saske");
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
                    .post("/api/v1/courier"); // отправь запрос на ручку
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
        deleteCourier(courier);
    }

    //чтобы создать курьера, нужно передать в ручку все обязательные поля
    //если одного из полей нет, запрос возвращает ошибку;
    @Test
    public void checkCreateCourierWithoutPassName(){
        Courier courier = new Courier("ya", "", "");
        Response response =
                given()
                    .header("Content-type", "application/json")// заполни header
                    .body(courier) // заполни body
                    .when()
                    .post("/api/v1/courier"); // отправь запрос на ручку
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    //чтобы создать курьера, нужно передать в ручку все обязательные поля
    //если одного из полей нет, запрос возвращает ошибку;
    @Test
    public void checkCreateCourierWithoutLoginName(){
        Courier courier = new Courier("", "1234", "");
        Response response =
                given()
                        .header("Content-type", "application/json")// заполни header
                        .body(courier) // заполни body
                        .when()
                        .post("/api/v1/courier"); // отправь запрос на ручку
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    //чтобы создать курьера, нужно передать в ручку все обязательные поля
    //если одного из полей нет, запрос возвращает ошибку;
    @Test
    public void checkCreateCourierWithoutLoginPass(){
        Courier courier = new Courier("", "", "saske");
        Response response =
                given()
                        .header("Content-type", "application/json")// заполни header
                        .body(courier) // заполни body
                        .when()
                        .post("/api/v1/courier"); // отправь запрос на ручку
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
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
