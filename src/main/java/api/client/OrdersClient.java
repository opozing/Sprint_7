package api.client;

import static io.restassured.RestAssured.given;
import static org.example.resources.Constants.*;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.CreateOrder;

public class OrdersClient {

    @Step("Create order {createOrder}")
    public ValidatableResponse create(CreateOrder createOrder) {
        return given()
                .header(HEADERS[0], HEADERS[1])
                .body(createOrder)
                .when()
                .post(BASE_URL + ORDER_URL)
                .then();
    }

    @Step("Get orderList {getOrder}")
    public ValidatableResponse getOrder() {
        return given()
                .header(HEADERS[0], HEADERS[1])
                .when()
                .get(ORDER_URL)
                .then();
    }
}
