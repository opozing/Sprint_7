import io.restassured.RestAssured;
import org.example.CreateCourier;
import org.example.LoginCourier;
import org.example.model.RandomGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class LoginCourierTest extends RandomGenerator {

    public int id;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }
    CreateCourier createCourier = new CreateCourier(getRandom(), getRandom(), getRandom());
    LoginCourier loginCourier = new LoginCourier(createCourier.getLogin(), createCourier.getPassword());

    @After
    public void tearDown() {
        given().delete("/api/v1/courier/" + id);
    }

    @Test
    public void courierCanLogin() {
        given()
                .header("Content-type", "application/json")
                .body(createCourier)
                .post("/api/v1/courier");

        id = given()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when().post("/api/v1/courier/login")
                .then().statusCode(200)
                .assertThat().body("id", notNullValue())
                .extract().path("id");
    }

    @Test
    public void courierCanNotLoginWithoutAnyData() {
        LoginCourier loginCourierWithoutLogin = new LoginCourier("", createCourier.getPassword());
        LoginCourier loginCourierWithoutPass = new LoginCourier(createCourier.getLogin(), "");

        given()
                .header("Content-type", "application/json")
                .body(createCourier)
                .post("/api/v1/courier");

        id = given()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when().post("/api/v1/courier/login")
                .then().extract().path("id");

        given()
                .header("Content-type", "application/json")
                .body(loginCourierWithoutLogin)
                .when().post("/api/v1/courier/login")
                .then().statusCode(400);

        given()
                .header("Content-type", "application/json")
                .body(loginCourierWithoutPass)
                .when().post("/api/v1/courier/login")
                .then().statusCode(400);
    }

    @Test
    public void courierCanNotLoginWithWrongData() {
        LoginCourier loginCourierWrongLogin = new LoginCourier(getRandom(),
                createCourier.getPassword());
        LoginCourier loginCourierWrongPass = new LoginCourier(createCourier.getLogin(),
                getRandom());

        given()
                .header("Content-type", "application/json")
                .body(createCourier)
                .post("/api/v1/courier");

        id = given()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when().post("/api/v1/courier/login")
                .then().extract().path("id");

        given()
                .header("Content-type", "application/json")
                .body(loginCourierWrongLogin)
                .when().post("/api/v1/courier/login")
                .then().statusCode(404)
                .assertThat().body("message", equalTo("Учетная запись не найдена"));

        given()
                .header("Content-type", "application/json")
                .body(loginCourierWrongPass)
                .when().post("/api/v1/courier/login")
                .then().statusCode(404)
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void canNotLoginWithNotExistCourier() {
        given()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when().post("/api/v1/courier/login")
                .then().statusCode(404)
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }
}
