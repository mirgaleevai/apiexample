package reqres.in.testexamples;

import org.junit.jupiter.api.Test;
import reqres.in.testexamples.models.CreateUserModel;
import reqres.in.testexamples.models.LoginBodyModel;
import reqres.in.testexamples.models.LoginResponseModel;
import reqres.in.testexamples.models.MissingPassModel;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static reqres.in.testexamples.helpers.CustomAllureListener.withCustomTemplates;
import static reqres.in.testexamples.specs.BaseSpecs.*;

public class TestExamples {

    LoginBodyModel authData = new LoginBodyModel();
    CreateUserModel createUser = new CreateUserModel();


    @Test
    void successfulLogin() {
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");

        LoginResponseModel loginResponse = step("Login with log/pass", () ->
                given(requestSpec)
                        .filter(withCustomTemplates())
                        .body(authData)
                        .post("/login")
                        .then()
                        .spec(responseSpec)
                        .extract().as(LoginResponseModel.class));

        step("Check response token", () ->
                assertEquals("QpwL5tke4Pnpja7X4", loginResponse.getToken()));

    }

    @Test
    void loginWithoutPassword() {
        authData.setEmail("eve.holt@reqres.in");

        MissingPassModel missPass = step("Login without password", () ->
                given(requestSpec)
                        .body(authData)
                        .post("/login")
                        .then()
                        .spec(responseSpecMissingPass)
                        .extract().as(MissingPassModel.class));

        step("Check response error", () ->
                assertEquals("Missing password", missPass.getError()));
    }

    @Test
    void getListUsersJsonValidate() {
        step("Get users list", () ->
                given(requestSpec)
                        .get("/users?page=2")
                        .then()
                        .spec(getListUsersSpec));
    }

    @Test
    void createUser() {
        createUser.setName("morpheus");
        createUser.setJob("leader");

        step("Create user", () ->
                given(requestSpec)
                        .body(createUser)
                        .post("/users")
                        .then()
                        .spec(CreateUserSpec));

        step("Check response", () -> {
            assertEquals("leader", createUser.getJob());
            assertEquals("morpheus", createUser.getName());
        });

    }

    @Test
    void updateUserJob() {
        createUser.setJob("zion resident");

        step("Update user job", () ->
                given(requestSpec)
                        .body(createUser)
                        .patch("/users/2")
                        .then()
                        .spec(UpdateUserSpec));

        step("Check response", () -> assertEquals("zion resident", createUser.getJob()));
    }

    @Test
    void deleteUser() {
        step("Delete user", () ->
                given(requestSpec)
                        .delete("/users/2")
                        .then()
                        .spec(DeleteUserSpec));
    }

}