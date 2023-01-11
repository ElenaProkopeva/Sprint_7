package org.example;

import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import org.example.BaseApi;
import org.example.Courier;
import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.*;

public class CourierApi extends BaseApi {

    private final Courier courier;
    private final RequestSpecification requestSpecification;

    public CourierApi(RequestSpecification requestSpecification) {
        this.courier = new Courier("ya", "1234", "saske");
        this.requestSpecification = requestSpecification;
    }

    @Step("Создать курьера и проверить статус ответа")
    public Courier createCourier(){
        //   this.courier = new Courier("ya", "1234", "saske");
        requestSpecification
                .given()
                .body(courier) // заполни body
                .when()
                .post(COURIER_CREATE_ENDPOINT) // отправь запрос на ручку
                .then().assertThat().body("ok", is(true))
                .and()
                .statusCode(SC_CREATED);
        return courier;
    }

    @Step("Авторизоваться под курьером и получить его id")
    public void loginCourier(){
        requestSpecification
                .given()
                .body(courier) // заполни body
                .when()
                .post(COURIER_LOGIN_ENDPOINT) // отправь запрос на ручку
                .then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

    @Step("Удалить курьера по id")
    public void deleteCourier(){
        if (courier != null) {
            Integer courierId =
                    requestSpecification.given()
                            .body(courier) // заполни body
                            .when()
                            .post(COURIER_LOGIN_ENDPOINT) // отправь запрос на ручку
                            .then().extract().body().path("id");
            requestSpecification.given()
                    .delete(COURIER_CREATE_ENDPOINT + "/{id}", courierId.toString()) // отправка DELETE-запроса
                    .then().assertThat().statusCode(SC_OK); // проверка, что сервер вернул код 200
        }
    }
}
