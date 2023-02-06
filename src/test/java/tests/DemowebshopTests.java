package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.AddWishlistResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import pages.InfoPage;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

public class DemowebshopTests {

    private static String authCookies = "06DDE012FDA76EDCA25010E855842BED1B33D42DEE6CCFF2E7C95ABA940427D" +
            "641D4F6F72714D4F9794E0D0B0C66707B7CF58DC23B379717E609EF29342B867A429E41DA32D574A304955C2BD6" +
            "3D1C6C812488C6B93CF6B054BCEE8B94549D12876425420F9D9A36A8C7ACEC5F78A7DBF9454148CD58D2BC019E5" +
            "2500155B056F468027053CF5AAFBFE4ED71AD990F3D";
    private static String URL = "https://demowebshop.tricentis.com";

    @BeforeAll
    public static void beforeAll() {
        RestAssured.baseURI = URL;
        Configuration.baseUrl = URL;

        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    @DisplayName("Успешная вторизация с использованием cookies")
    public void loginWithCookies() {

        String userEmail = "kirill_a@mail.ru",
                userPassword = "qwerty";

        step("Успешная вторизация с использованием cookies", () -> {

            String authCookie = given()
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("Email", userEmail)
                    .formParam("Password", userPassword)
                    .when()
                    .post("/login")
                    .then()
                    .statusCode(302)
                    .extract().cookie("NOPCOMMERCE.AUTH");

            step("Открыть страницу сервиса", () -> {
                open("/customer/info");
            });

            step("Загрузить cookie авторизации", () -> {
                getWebDriver().manage().addCookie(new Cookie("NOPCOMMERCE.AUTH", authCookie));
                refresh();
            });

            step("Проверка успешной авторизации (UI)", () -> {
                String userLoginActualUI = $(".header-links .account").getText();
                Assertions.assertEquals(userEmail, userLoginActualUI);
            });

            step("Проверка успешной авторизации (API)", () -> {
                Response response = given()
                        .contentType("text/html; charset=utf-8")
                        .cookie("NOPCOMMERCE.AUTH", authCookies)
                        .when()
                        .get("/customer/info")
                        .then()
                        .statusCode(200)
                        .extract().response();
                String userLoginActualAPI = response
                        .htmlPath()
                        .getString("**.find{it.@class == 'header-links'}.**.find{it.@class == 'account'}.text()");
                Assertions.assertEquals(userEmail, userLoginActualAPI);
            });
        });
    }

    @Test
    @DisplayName("Товар успешно добавлен в вишлист")
    public void addToWishlist() {

        InfoPage infoPage = new InfoPage();

        step("Авторизация в личном кабинете с помощью cookie", () -> {
            infoPage
                    .openPageWithCookies(new Cookie("NOPCOMMERCE.AUTH", authCookies))
                    .refreshPage();
        });

        step("Добавить товар в вишлист", () -> {

            int wishCountOld = infoPage.getWishQuantity();
            int wishCountActual;

            step("Выполнить запрос на добавление 1 (одного) товара в вишлист", () -> {

                AddWishlistResponse addWishlistResponse = given()
                        .contentType("application/x-www-form-urlencoded")
                        .cookie("NOPCOMMERCE.AUTH", authCookies)
                        .body("addtocart_43.EnteredQuantity=1")
                        .when()
                        .post("/addproducttocart/details/43/2")
                        .then().log().all()
                        .statusCode(200)
                        .extract().as(AddWishlistResponse.class);
                Assertions.assertEquals(true, addWishlistResponse.getSuccess());
                Assertions.assertTrue(addWishlistResponse.getMessage().contains("The product has been added to your"));
            });

            infoPage.refreshPage();
            wishCountActual = infoPage.getWishQuantity();
            Assertions.assertEquals(wishCountActual - wishCountOld, 1);
        });
    }
}
