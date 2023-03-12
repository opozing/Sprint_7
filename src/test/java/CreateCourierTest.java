import io.restassured.RestAssured;
import org.example.CreateCourier;
import org.example.LoginCourier;
import org.example.model.RandomGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CreateCourierTest extends RandomGenerator {

    public int id;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @After
    public void teardown() {
        given().delete("/api/v1/courier/" + id);
    }

    @Test
    public void canCreateNewCourier() {

        CreateCourier createCourier = new CreateCourier(getRandom(), getRandom(), getRandom());
        LoginCourier loginCourier = new LoginCourier(createCourier.getLogin(), createCourier.getPassword());

        given()
                .header("Content-type", "application/json")
                .body(createCourier)
                .when().post("/api/v1/courier")
                .then().statusCode(201)
                .assertThat().body("ok", is(true));

        id = given()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when().post("/api/v1/courier/login")
                .then().statusCode(200)
                .extract().body().path("id");
    }

    @Test
    public void canNotCreateCourierWithSameData() {
        CreateCourier createCourier = new CreateCourier(getRandom(), getRandom(), getRandom());
        LoginCourier loginCourier = new LoginCourier(createCourier.getLogin(), createCourier.getPassword());

        given()
                .header("Content-type", "application/json")
                .body(createCourier)
                .post("/api/v1/courier");

        given()
                .header("Content-type", "application/json")
                .body(createCourier)
                .when().post("/api/v1/courier")
                .then().statusCode(409)
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        id = given()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when().post("/api/v1/courier/login")
                .then().extract().body().path("id");
    }

    @Test
    public void canNotCreateCourierWithSameLogin() {
        CreateCourier createCourier = new CreateCourier(getRandom(), getRandom(), getRandom());
        CreateCourier createCourierWithSameLogin = new CreateCourier(createCourier.getLogin(),
                getRandom(), getRandom());
        LoginCourier loginCourier = new LoginCourier(createCourier.getLogin(), createCourier.getPassword());

        given()
                .header("Content-type", "application/json")
                .body(createCourier)
                .post("/api/v1/courier");

        given()
                .header("Content-type", "application/json")
                .body(createCourierWithSameLogin)
                .when().post("/api/v1/courier")
                .then().statusCode(409)
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        id = given()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when().post("/api/v1/courier/login")
                .then().extract().body().path("id");
    }

    @Test
    public void canNotCreateCourierWithoutMusthaveData() {
        CreateCourier courierWithoutLogin = new CreateCourier(null, getRandom(), getRandom());
        CreateCourier courierWithoutPass = new CreateCourier(getRandom(), null, getRandom());

        given()
                .header("Content-type", "application/json")
                .body(courierWithoutLogin)
                .when().post("/api/v1/courier")
                .then().statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));

        given()
                .header("Content-type", "application/json")
                .body(courierWithoutPass)
                .when().post("/api/v1/courier")
                .then().statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
