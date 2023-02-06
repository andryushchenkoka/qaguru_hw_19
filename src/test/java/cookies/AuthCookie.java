package cookies;

import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.refresh;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class AuthCookie {

    public static void addCookie(Cookie cookie) {
        getWebDriver().manage().addCookie(cookie);
        refresh();
    }
}
