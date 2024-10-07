import helpers.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import object.UserSteps;
import helpers.UserGenerator;

import static org.hamcrest.Matchers.*;

public class UserAuthTests {
    private final UserSteps userSteps = new UserSteps();
    private final User user = UserGenerator.randomUser();
    private String accessToken;

    @Before
    public void setUpUserAuth() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        accessToken = userSteps
                .userCreate(user)
                .body("accessToken", notNullValue())
                .extract().path("accessToken");
    }
    @Test
    @DisplayName("Авторизация под существующим пользователем")
    public void authUserAuthOkCheck() {
        userSteps
                .userAuthorization(user)
                .statusCode(200)
                .body("success", is(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", notNullValue())
                .body("user.name", notNullValue());
    }

    @Test
    @DisplayName("Авторизация с неверным email и password")
    public void authWithIncorrectEmailAndPasswordCheck() {
        user.setEmail(RandomStringUtils.randomAlphabetic(6));
        user.setPassword(RandomStringUtils.randomAlphabetic(6));
        userSteps
                .userAuthorization(user)
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с неверным email")
    public void authWithIncorrectEmailCheck() {
        user.setEmail(RandomStringUtils.randomAlphabetic(6));
        userSteps
                .userAuthorization(user)
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с неверным password")
    public void authIncorrectPasswordCheck() {
        user.setPassword(RandomStringUtils.randomAlphabetic(6));
        userSteps
                .userAuthorization(user)
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    @DisplayName("Удаление созданного пользователя")
    public void tearDownUserAuth() {
        userSteps
                .userDelete(accessToken);
    }

}

