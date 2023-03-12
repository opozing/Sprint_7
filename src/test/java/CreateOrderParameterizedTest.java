import io.restassured.RestAssured;
import org.example.CreateOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.notNullValue;


@RunWith(Parameterized.class)
public class CreateOrderParameterizedTest {

    private final List<String> colour;

    public CreateOrderParameterizedTest(List<String> colour) {
        this.colour = colour;
    }

    @Parameterized.Parameters
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
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void canCreateOrder() {
        CreateOrder createOrder = new CreateOrder("1", "2", "3", 1, "4",
                2, "12-12-2000", "5", colour);

        given()
                .header("Content-type", "application/json")
                .body(createOrder)
                .when().post("/api/v1/orders")
                .then().statusCode(201)
                .body("$", hasKey("track"))
                .assertThat().body("track", notNullValue());
    }
}
