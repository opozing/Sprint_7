import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;


public class GetOrderListTest {

    List<String> orders;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void getOrderListReturnsOrders() {
    orders = given()
            .header("Content-type", "application/json")
            .when().get("/api/v1/orders")
            .then().statusCode(200)
            .extract().body().path("orders");
    Assert.assertThat(orders, notNullValue());
    }
}
