package pages;

import cookies.AuthCookie;
import org.openqa.selenium.Cookie;
import pages.components.Header;

import static com.codeborne.selenide.Selenide.*;

public class InfoPage {

    private final String URL = "/customer/info";

    public InfoPage openPageWithCookies(Cookie cookie) {
        open(URL);
        AuthCookie.addCookie(cookie);
        return this;
    }

    public InfoPage openPage() {
        open(URL);
        return this;
    }

    public InfoPage addCookies(Cookie cookie) {
        AuthCookie.addCookie(cookie);
        return this;
    }

    public InfoPage refreshPage() {
        refresh();
        return this;
    }

    public int getWishQuantity() {
        return Header.getWishQuantity();
    }

    public String getProfileName() {
        return Header.getProfileName();
    }
}
