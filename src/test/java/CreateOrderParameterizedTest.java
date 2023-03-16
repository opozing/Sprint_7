import api.client.OrdersClient;
import io.qameta.allure.junit4.DisplayName;
import org.example.CreateOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.List;
import static org.hamcrest.Matchers.*;


@RunWith(Parameterized.class)
public class CreateOrderParameterizedTest {

    private final List<String> colour;
    public OrdersClient ordersClient;

    public CreateOrderParameterizedTest(List<String> colour) {
        this.colour = colour;
    }

    @Parameterized.Parameters(name = "Цвет самоката. Тестовые данные: {0} {1} {2} {3}")
    public static Object[][] getData() {
        return new Object[][] {
                new List[]{List.of("GRAY")},
                new List[]{List.of("BLACK")},
                new List[]{List.of("GRAY, BLACK")},
                new List[]{List.of("")}
        };
    }

    @Before
    public void setUp() {
        ordersClient = new OrdersClient();
    }

    @Test
    @DisplayName("Заказ можно создать с разными цветами")
    public void canCreateOrderWithAnyColours() {
        CreateOrder createOrder = new CreateOrder("1", "2", "3", 1, "4",
                2, "12-12-2000", "5", colour);

        ordersClient.create(createOrder)
                .assertThat().statusCode(201)
                .and()
                .body("$", hasKey("track"))
                .and()
                .body("track", notNullValue());
    }
}
