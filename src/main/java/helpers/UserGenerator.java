package helpers;

import com.github.javafaker.Faker;
public class UserGenerator {
    private static Faker faker = new Faker();

    public static User randomUser() {
        return new User(
        faker.internet().emailAddress(),
        faker.internet().password(),
        faker.name().firstName()
        );
    }
}
