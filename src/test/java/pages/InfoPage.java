package pages;

import com.codeborne.selenide.SelenideElement;
import cookies.AuthCookie;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.*;

public class InfoPage {

    private final SelenideElement wishlistQuantity = $("span.wishlist-qty");
    private final String URL = "/customer/info";

    public InfoPage openPageWithCookies(Cookie cookie) {
        open(URL);
        AuthCookie.addCookie(cookie);
        return this;
    }

    public InfoPage refreshPage() {
        refresh();
        return this;
    }

    public int getWishQuantity() {
        String strQuantity = wishlistQuantity.getText().replaceAll("\\D", "");
        return Integer.parseInt(strQuantity);
    }
}
