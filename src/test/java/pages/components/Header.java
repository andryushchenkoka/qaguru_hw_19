package pages.components;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class Header {

    private static final SelenideElement wishlistQuantity = $("span.wishlist-qty"),
            profileName = $(".header-links .account");

    public static int getWishQuantity() {
        String strQuantity = wishlistQuantity.getText().replaceAll("\\D", "");
        return Integer.parseInt(strQuantity);
    }

    public static String getProfileName() {
        return profileName.getText();
    }
}
