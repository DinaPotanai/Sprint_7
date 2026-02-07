package tests;

import api.OrderApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderTest {

    private final OrderApi orderApi = new OrderApi();
    private String track;

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что в тело ответа возвращается список заказов")
    public void getOrdersTest() {
        Response response = orderApi.getOrders();
        response.then()
                .assertThat()
                .statusCode(HTTP_OK)
                .body("orders", is(notNullValue()));
    }
}
