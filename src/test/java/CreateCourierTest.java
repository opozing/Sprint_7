import io.qameta.allure.junit4.DisplayName;
import api.client.CourierClient;
import org.example.CreateCourier;
import org.example.LoginCourier;
import org.example.model.RandomGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class CreateCourierTest extends RandomGenerator {

    public int id;
    public CourierClient courierClient;

    @Before
    public void setUp() {
//        RestAssured.baseURI = BASE_URL;
        courierClient = new CourierClient();
    }
    @After
    public void teardown() {
        courierClient.delete(id);
    }

    @Test
    @DisplayName("Курьера можно создать")
    public void canCreateNewCourier() {
        CreateCourier createCourier = new CreateCourier(getRandom(), getRandom(), getRandom());
        LoginCourier loginCourier = new LoginCourier(createCourier.getLogin(), createCourier.getPassword());

        courierClient.create(createCourier)
                .statusCode(201)
                .assertThat().body("ok", is(true));

        id = courierClient.login(loginCourier)
                .statusCode(200)
                .extract().body().path("id");
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void canNotCreateCourierWithSameData() {
        CreateCourier createCourier = new CreateCourier(getRandom(), getRandom(), getRandom());
        LoginCourier loginCourier = new LoginCourier(createCourier.getLogin(), createCourier.getPassword());

        courierClient.create(createCourier);
        id = courierClient.login(loginCourier)
                .extract().body().path("id");
        courierClient.create(createCourier)
                .statusCode(409)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Нельзя создать курьера если login уже занят")
    public void canNotCreateCourierWithSameLogin() {
        CreateCourier createCourier = new CreateCourier(getRandom(), getRandom(), getRandom());
        CreateCourier createCourierWithSameLogin = new CreateCourier(createCourier.getLogin(),
                getRandom(), getRandom());
        LoginCourier loginCourier = new LoginCourier(createCourier.getLogin(), createCourier.getPassword());

        courierClient.create(createCourier);
        id = courierClient.login(loginCourier)
                .extract().body().path("id");
        courierClient.create(createCourierWithSameLogin)
                .assertThat().statusCode(409)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Нельзя создать курьера без обязательного поля")
    public void canNotCreateCourierWithoutMusthaveData() {
        CreateCourier courierWithoutLogin = new CreateCourier(null, getRandom(), getRandom());
        CreateCourier courierWithoutPass = new CreateCourier(getRandom(), null, getRandom());

        courierClient.create(courierWithoutLogin)
                .assertThat().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

        courierClient.create(courierWithoutPass)
                .assertThat().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
