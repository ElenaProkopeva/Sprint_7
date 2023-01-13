package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.Courier;
import static org.apache.http.HttpStatus.*;

public class CourierApi{

    private final static String COURIER_CREATE_ENDPOINT = "/courier";
    private final static String COURIER_LOGIN_ENDPOINT = "/courier/login";
    private Courier courier;
    private final RequestSpecification requestSpecification;

    public CourierApi(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    @Step("Создать курьера и проверить статус ответа")
    public Response createCourier(){
        Response response =
            requestSpecification
                .given()
                .body(courier) // заполни body
                .when()
                .post(COURIER_CREATE_ENDPOINT); // отправь запрос на ручку
        return response;
    }

    @Step("Авторизоваться под курьером и получить его id")
    public Response loginCourier(){
        Response response =
            requestSpecification
                .given()
                .body(courier) // заполни body
                .when()
                .post(COURIER_LOGIN_ENDPOINT); // отправь запрос на ручку
        return response;
    }

    @Step("Удалить курьера по id")
    public void deleteCourier(){
        Integer courierId =
                    requestSpecification.given()
                            .body(courier) // заполни body
                            .when()
                            .post(COURIER_LOGIN_ENDPOINT) // отправь запрос на ручку
                            .then().extract().body().path("id");
        if (courierId != null) {
            requestSpecification.given()
                    .delete(COURIER_CREATE_ENDPOINT + "/{id}", courierId.toString()) // отправка DELETE-запроса
                    .then().assertThat().statusCode(SC_OK); // проверка, что сервер вернул код 200
        }
    }
}
