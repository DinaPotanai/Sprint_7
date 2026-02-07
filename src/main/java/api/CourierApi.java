package api;

import data.Courier;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierApi extends BaseHttpClient {

    private static final String CREATE_COURIER_URL = "/api/v1/courier";
    private static final String LOGIN_COURIER_URL = "/api/v1/courier/login";
    private static final String DELETE_COURIER_URL = "/api/v1/courier/{id}";

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given()
                .spec(baseRequestSpec())
                .body(courier)
                .when()
                .post(CREATE_COURIER_URL);
    }

    @Step("Логин курьера в системе")
    public Response loginCourier(Courier courier) {
        return given()
                .spec(baseRequestSpec())
                .body(courier)
                .when()
                .post(LOGIN_COURIER_URL);
    }

    @Step("Удаление курьера")
    public Response deleteCourier(int id) {
        return given()
                .spec(baseRequestSpec())
                .pathParam("id", id)
                .when()
                .delete(DELETE_COURIER_URL);
    }

}
