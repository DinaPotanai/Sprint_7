package api;

import data.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderApi extends BaseHttpClient {

    public static final String CREATE_ORDER_URL = "/api/v1/orders";
    public static final String GET_ORDERS_URL = "/api/v1/orders";
    public static final String DELETE_ORDER_URL = "/api/v1/orders/cancel";

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return given()
                .spec(baseRequestSpec())
                .and()
                .body(order)
                .when()
                .post(CREATE_ORDER_URL);
    }

    @Step("Получение заказа")
    public Response getOrders() {
        return given()
                .spec(baseRequestSpec())
                .when()
                .get(GET_ORDERS_URL);
    }

    @Step("Удаление заказа")
    public Response deleteOrder(String track) {
        String requestBody = String.format("{\"track\":\"%s\"}", track);

        return given()
                .spec(baseRequestSpec())
                .body(requestBody)
                .when()
                .put(DELETE_ORDER_URL);
    }

}
