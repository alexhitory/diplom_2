package helpers;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;

import static helpers.BaseUrl.BaseUrl;
import static io.restassured.http.ContentType.JSON;

public class Config {
    public static RequestSpecification spec() {
        return new RequestSpecBuilder()
                .setContentType(JSON)
                .setBaseUri(BaseUrl)
                .log(LogDetail.ALL)
                .build();
    }
}