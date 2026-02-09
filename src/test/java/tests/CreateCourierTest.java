package tests;

import api.CourierApi;
import data.Courier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateCourierTest {

    CourierApi courierApi = new CourierApi();
    int courierId = 0;

    @Test
    @DisplayName("Создание курьера")
    @Description("Проверка, что можносоздать курьера с корректными данными")
    public void courierCreatedTest() {
        String uniqueLogin = "Courier_test" + System.currentTimeMillis();
        Courier courier = new Courier(uniqueLogin, "qwerty123", "Рафаэль");
        Response response = courierApi.createCourier(courier);
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_CREATED)
                .body("ok", equalTo(true));
        Response loginResponse = courierApi.loginCourier(courier);
        loginResponse.then().log().all()
                .assertThat()
                .statusCode(HTTP_OK)
                .body("id", notNullValue());
        courierId = loginResponse.then().extract().path("id");
    }

    @Test
    @DisplayName("Создание одинаковых курьеров невозможно")
    @Description("Проверка, что нельзя создать двух курьеров с одинаковым логином")
    public void identicalCourierCreatedTest() {
        String login = "DuplicateCourierNew";
        Courier courier = new Courier(login, "123", "Микеланджело");
        Response response = courierApi.createCourier(courier);
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_CREATED)
                .body("ok", equalTo(true));
        Response loginResponse = courierApi.loginCourier(courier);
        loginResponse.then().log().all()
                .assertThat()
                .statusCode(HTTP_OK)
                .body("id", notNullValue());
        courierId = loginResponse.then().extract().path("id");
        Courier courierDuplicate = new Courier(login, "678", "Антонио");
        Response responseDuplicate = courierApi.createCourier(courierDuplicate);
        responseDuplicate.then().log().all()
                .assertThat()
                .statusCode(HTTP_CONFLICT)
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка, что нельзя создать курьера без одного из обязательных полей (логина)")
    public void courierCreatedWithoutLoginTest() {
        Courier courier= new Courier(null, "password", "Донателло");
        Response response = courierApi.createCourier(courier);
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка, что нельзя создать курьера без одного из обязательных полей (пароля)")
    public void courierCreatedWithoutPasswordTest() {
        Courier courier= new Courier("Courier_test", null, "Леонардо");
        Response response = courierApi.createCourier(courier);
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void cleanUp() {
        if (courierId != 0) {
            courierApi.deleteCourier(courierId)
                    .then()
                    .statusCode(HTTP_OK);
        }
    }
}
