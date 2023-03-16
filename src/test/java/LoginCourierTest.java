import api.client.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import org.example.CreateCourier;
import org.example.LoginCourier;
import org.example.model.RandomGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class LoginCourierTest extends RandomGenerator {

    public int id;
    public CourierClient courierClient;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }
    CreateCourier createCourier = new CreateCourier(getRandom(), getRandom(), getRandom());
    LoginCourier loginCourier = new LoginCourier(createCourier.getLogin(), createCourier.getPassword());


    @After
    public void tearDown() {
        courierClient.delete(id);
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    public void courierCanLogin() {
        courierClient.create(createCourier);
        id = courierClient.login(loginCourier)
                .assertThat().statusCode(200)
                .and()
                .body("id", notNullValue())
                .extract().body().path("id");
    }

    @Test
    @DisplayName("Курьер не может авторизоваться без обязательных полей")
    public void courierCanNotLoginWithoutAnyData() {
        LoginCourier loginCourierWithoutLogin = new LoginCourier("", createCourier.getPassword());
        LoginCourier loginCourierWithoutPass = new LoginCourier(createCourier.getLogin(), "");

        courierClient.create(createCourier);
        id = courierClient.login(loginCourier)
                .extract().body().path("id");
        courierClient.login(loginCourierWithoutLogin)
                .assertThat().statusCode(400);
        courierClient.login(loginCourierWithoutPass)
                .assertThat().statusCode(400);
    }

    @Test
    @DisplayName("Курьер не может авторизоваться с не валидными полями")
    public void courierCanNotLoginWithWrongData() {
        LoginCourier loginCourierWrongLogin = new LoginCourier(getRandom(),
                createCourier.getPassword());
        LoginCourier loginCourierWrongPass = new LoginCourier(createCourier.getLogin(),
                getRandom());

        courierClient.create(createCourier);
        id = courierClient.login(loginCourier)
                .extract().body().path("id");
        courierClient.login(loginCourierWrongLogin)
                .assertThat().statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
        courierClient.login(loginCourierWrongPass)
                .assertThat().statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Несуществующий курьер не может авторизоваться")
    public void canNotLoginWithNotExistCourier() {
        courierClient.login(loginCourier)
                .assertThat().statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }
}
