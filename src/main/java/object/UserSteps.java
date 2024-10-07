package object;

import helpers.Config;
import helpers.User;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static helpers.EndPoints.*;
import static io.restassured.RestAssured.given;
public class UserSteps {
    @Step("Создание пользователя")
    public ValidatableResponse userCreate(User user) {
        return given()
                .spec(Config.spec())
                .body(user)
                .when()
                .post(USER_CREATE)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse userAuthorization(User user) {
        return given()
                .spec(Config.spec())
                .body(user)
                .when()
                .post(USER_LOGIN)
                .then();
    }

    @Step("Изменение данных пользователя с авторизацией")
    public ValidatableResponse userDataEdit(User user, String accessToken) {
        return given()
                .spec(Config.spec())
                .headers("Authorization", accessToken)
                .body(user)
                .when()
                .patch(USER_EDIT)
                .then();
    }

    @Step("Изменение данных пользователя без авторизации")
    public ValidatableResponse userDataEditWithoutAuth(User user) {
        return given()
                .spec(Config.spec())
                .body(user)
                .when()
                .patch(USER_EDIT)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse userDelete(String accessToken) {
        if (accessToken != null) {
            return given()
                    .spec(Config.spec())
                    .headers("authorization", accessToken)
                    .delete(USER_EDIT)
                    .then()
                    .statusCode(202);
        }
        return null;
    }
}
