package reqres.in.testexamples;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class TestExamples extends TestBase {

    @Test
    void successfulLogin() {
        given()
                .contentType(JSON)
                .log().all()
                .body(" {\"email\":\"eve.holt@reqres.in\", \"password\": \"cityslicka\" } ")
                .post( "/login")
                .then()
                .statusCode(200)
                .log().status()
                .log().body()
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void loginWithoutPassword() {
        given()
                .contentType(JSON)
                .log().all()
                .body(" {\"email\":\"sydney@fife\"} ")
                .post("/login")
                .then()
                .statusCode(400)
                .log().status()
                .log().body()
                .body("error", is("Missing password"));
    }

    @Test
    void getListUsers() {
        given()
                .contentType(JSON)
                .log().all()
                .get( "/users?page=2")
                .then()
                .statusCode(200)
                .log().status()
                .log().body()
                .body("data.email", hasItems("michael.lawson@reqres.in"));
    }

    @Test
    void createUser() {
        given()
                .contentType(JSON)
                .log().all()
                .body(" {\"name\": \"morpheus\", \"job\": \"leader\"} ")
                .post("/users")
                .then()
                .statusCode(201)
                .log().status()
                .log().body()
                .body("name", is("morpheus"));
    }

    @Test
    void updateUser() {
        given()
                .contentType(JSON)
                .log().all()
                .body(" {\"name\": \"morpheus\", \"job\": \"zion resident\"} ")
                .patch("/users/2")
                .then()
                .statusCode(200)
                .log().status()
                .log().body()
                .body("job", is("zion resident"));
    }

    @Test
    void deleteUser() {
        given()
                .contentType(JSON)
                .log().all()
                .delete("/users/2")
                .then()
                .statusCode(204)
                .log().status()
                .log().body();
    }

}