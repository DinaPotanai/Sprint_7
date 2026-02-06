package tests;

import api.OrderApi;
import data.Order;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderParamTest {

    private final List<String> colors;
    private final OrderApi orderApi = new OrderApi();
    private String track;

    public CreateOrderParamTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "{index}: Тест с цветом {0}")
    public static Object[][] getData() {
        return new Object[][]{
                {Collections.emptyList()},           // случай без указания цвета
                {List.of("BLACK")},              // только цвет BLACK
                {List.of("GREY")},               // только цвет GREY
                {Arrays.asList("BLACK", "GREY")}     // оба цвета указаны
        };
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка, что можно создать заказы с разными цветами самоката")
    public void createOrderTest() {
        Order order = new Order("Леонардо",
                "Дикаприо",
                "Голливуд",
                "Щукинская",
                "89160000000",
                1,
                "05.02.2026",
                "без комментариев",
                colors);
        Response response = orderApi.createOrder(order);
        response.then().log().all()
                .assertThat()
                .statusCode(201)
                .body("track", notNullValue());

    }

    @Test
    @DisplayName("Список заказов")
    @Description("Проверка, что в тело ответа возвращается список заказов")
    public void getOrdersTest() {
        Response response = orderApi.getOrders();
        response.then()
                .assertThat()
                .statusCode(200)
                .body("orders", is(notNullValue()));
    }

    @After
    public void cleanUpTest() {
        if (track != null) {
            Response deleteResponse = orderApi.deleteOrder(track);
            deleteResponse.then()
                    .assertThat()
                    .statusCode(200);
        }
    }
}
