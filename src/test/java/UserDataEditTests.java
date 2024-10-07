import com.github.javafaker.Faker;
import helpers.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import object.UserSteps;
import helpers.UserGenerator;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Изменение данных пользователя")
public class UserDataEditTests {
    private final UserSteps userSteps = new UserSteps();
    private final User user = UserGenerator.randomUser();
    private String accessToken;
    Faker faker = new Faker();

    @Before
    public void setUpUserDataEdit() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        accessToken = userSteps
                .userCreate(user)
                .body("accessToken", notNullValue())
                .extract().path("accessToken");
    }

    @Test
    @DisplayName("Изменение email пользователя с авторизацией")
    public void userAuthEmailEditOkCheck() {
        String newEmail = faker.internet().emailAddress();
        user.setEmail(newEmail);
        ValidatableResponse validatableResponse = userSteps
                .userDataEdit(user, accessToken)
                .assertThat()
                .statusCode(200)
                .body("success", is(true));
        String emailChanged = validatableResponse
                .extract()
                .path("user.email");
        Assert.assertEquals(user.getEmail(), emailChanged);
    }

    @Test
    @DisplayName("Изменение name пользователя с авторизацией")
    public void userAuthNameEditOkCheck() {
        String newName = faker.name().firstName();
        user.setName(newName);
        ValidatableResponse validatableResponse = userSteps
                .userDataEdit(user, accessToken)
                .statusCode(200)
                .body("success", is(true));
        String nameChanged = validatableResponse
                .extract()
                .path("user.name");
        Assert.assertEquals(user.getName(), nameChanged);
    }

    @Test
    @DisplayName("Изменение email пользователя без авторизации")
    public void userNotAuthEmailEditFailCheck() {
        String newEmail = faker.internet().emailAddress();
        user.setEmail(newEmail);
        userSteps
                .userDataEditWithoutAuth(user)
                .assertThat()
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение name пользователя без авторизации")
    public void userNotAuthNameEditFailCheck() {
        String newName = faker.name().firstName();
        user.setName(newName);
        userSteps
                .userDataEditWithoutAuth(user)
                .assertThat()
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @After
    @DisplayName("Удаление созданного пользователя")
    public void tearDownDataEdit() {
        userSteps
                .userDelete(accessToken);
    }

}