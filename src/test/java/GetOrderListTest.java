import api.client.OrdersClient;
import io.qameta.allure.junit4.DisplayName;
import static org.example.resources.Constants.*;
import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;


public class GetOrderListTest {

    public OrdersClient ordersClient;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        ordersClient = new OrdersClient();
    }

    @Test
    @DisplayName("Возвращается список заказов")
    public void getOrderListReturnsOrders() {
        List<String> ordersList = ordersClient.getOrder()
                .assertThat().statusCode(200)
                .and()
                .extract().path("orders");
        Assert.assertNotNull(ordersList);
    }
}
