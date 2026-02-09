package tests;

import api.CourierApi;
import data.Courier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.net.HttpURLConnection.*;
        import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {

    CourierApi courierApi = new CourierApi();
    private int courierId;
    private Courier courier;

    @Before
    public void setUp() {
        String uniqueLogin = "Courier_test" + System.currentTimeMillis();
        this.courier = new Courier(uniqueLogin, "qwerty123", "Рафаэль");
        Response response = courierApi.createCourier(this.courier);
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    @Description("Проверка, что курьер может авторизоваться с корректными данными, в ответе возвращается id")
    public void courierCanLoginTest() {
        Courier validCourier = new Courier(courier.getLogin(), courier.getPassword());
        Response loginResponse = courierApi.loginCourier(validCourier);
        loginResponse.then().log().all()
                .assertThat()
                .statusCode(HTTP_OK)
                .body("id", notNullValue());
        courierId = loginResponse.then().extract().path("id");
    }

    @Test
    @DisplayName("Курьер не может авторизоваться")
    @Description("Проверка, что курьер не может авторизоваться с несуществующими данными")
    public void courierCanNotLoginTest() {
        Courier incorrectCourier = new Courier("0000", "Шреддер");
        Response response = courierApi.loginCourier(incorrectCourier);
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Курьер не может авторизоваться без логина")
    @Description("Проверка, что курьер не может авторизоваться без логина")
    public void loginWithoutLoginLoginTest() {
        Response responseMissingLogin = courierApi.loginCourier(new Courier(null, "password"));
        responseMissingLogin.then().log().all()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Курьер не может авторизоваться с несуществующим паролем")
    @Description("If you log in under a non-existent user, the request returns an error")
    public void loginFailsForNonExistentLoginTest() {
        Courier nonExistentCourier = new Courier("Сплинтер", "qwerty123");
        Response response = courierApi.loginCourier(nonExistentCourier);
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
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
