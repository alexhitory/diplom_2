import helpers.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import object.OrderSteps;
import object.UserSteps;
import helpers.Burger;
import helpers.UserGenerator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Получение заказов конкретного пользователя")
public class UserListOrderTests {
    private final UserSteps userSteps = new UserSteps();
    private final User user = UserGenerator.randomUser();
    private final OrderSteps orderSteps = new OrderSteps();
    private String accessToken;
    Burger burgerWithCorrectIngredients = Burger.burgerWithCorrectIngredients();

    @Before
    @DisplayName("Создание пользователя и его авторизация")
    public void setUpUserListOrder() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        accessToken = userSteps
                .userCreate(user)
                .body("accessToken", notNullValue())
                .extract().path("accessToken");
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void userListOrderWithAuthCheck() {
        orderSteps
                .createOrderWithAuth(burgerWithCorrectIngredients, accessToken);
        ValidatableResponse validatableResponse = orderSteps
                .getUserListOrdersWithAuth(accessToken);
        validatableResponse
                .assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("orders", notNullValue())
                .body("total", notNullValue())
                .body("totalToday", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void userListOrderWithoutAuthCheck() {
        orderSteps
                .getUserListOrdersWithoutAuth()
                .assertThat()
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
    }


    @After
    @DisplayName("Удаление созданного пользователя")
    public void tearDownOrderCreate() {
        userSteps
                .userDelete(accessToken)
                .statusCode(202);
    }

}