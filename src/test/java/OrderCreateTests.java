import helpers.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;

import object.OrderSteps;
import object.UserSteps;

import helpers.Burger;
import helpers.UserGenerator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class OrderCreateTests {
    private final User user = UserGenerator.randomUser();
    private final UserSteps userSteps = new UserSteps();
    private final OrderSteps orderSteps = new OrderSteps();
    Burger emptyBurger = new Burger();
    private String accessToken;
    Burger burgerWithCorrectIngredients = Burger.burgerWithCorrectIngredients();
    Burger burgerWithIncorrectIngredients = Burger.burgerWithIncorrectIngredients();


    @Before
    public void setUpOrderCreate() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        accessToken = userSteps
                .userCreate(user)
                .body("accessToken", notNullValue())
                .extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами, с авторизацией")
    public void orderCreateWithIngredientsWithAuth() {
        ValidatableResponse validatableResponse = orderSteps
                .createOrderWithAuth(burgerWithCorrectIngredients, accessToken);
        validatableResponse
                .assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void orderCreateWithoutAuth() {
        ValidatableResponse validatableResponse = orderSteps
                .createOrderWithoutAuth(burgerWithCorrectIngredients);
        validatableResponse
                .assertThat()
                .statusCode(401)
                .body("success", is(false));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов, с авторизацией")
    public void orderCreateWithoutIngredientsWithAuth() {
        ValidatableResponse validatableResponse = orderSteps
                .createOrderWithAuth(emptyBurger, accessToken);
        validatableResponse
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void orderCreateWithInvalidHashCheck() {
        ValidatableResponse validatableResponse = orderSteps
                .createOrderWithAuth(burgerWithIncorrectIngredients, accessToken);
        validatableResponse
                .assertThat()
                .statusCode(500);
    }

    @After
    @DisplayName("Удаление созданного пользователя")
    public void tearDownOrderCreate() {
        userSteps
                .userDelete(accessToken)
                .statusCode(202);
    }

}
