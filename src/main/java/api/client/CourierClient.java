package api.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.CreateCourier;
import org.example.LoginCourier;
import static org.example.resources.Constants.*;
import static io.restassured.RestAssured.given;


public class CourierClient {

    @Step("Create courier {createCourier}")
    public ValidatableResponse create(CreateCourier createCourier) {
        return given()
                .header(HEADERS[0], HEADERS[1])
                .body(createCourier)
                .when()
                .post(BASE_URL + COURIER_URL)
                .then();
    }

    @Step("Login as {loginCourier}")
    public ValidatableResponse login(LoginCourier loginCourier) {
        return given()
                .header(HEADERS[0], HEADERS[1])
                .body(loginCourier)
                .when()
                .post(BASE_URL + COURIER_LOGIN_URL)
                .then();
    }

    @Step("Delete courier {id}")
    public ValidatableResponse delete(int id) {
        return given()
                .header(HEADERS[0], HEADERS[1])
                .when()
                .delete(BASE_URL + COURIER_URL + id)
                .then();
    }
}
