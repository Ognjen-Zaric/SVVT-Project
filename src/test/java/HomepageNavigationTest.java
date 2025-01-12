import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;

public class HomepageNavigationTest {

    private static WebDriver driver;
    private static String baseUrl;

    private boolean isLinkWorking(String linkUrl) throws IOException {
        try {
            URL url = new URL(linkUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();

            int responseCode = connection.getResponseCode();
            System.out.println("Response code: " + responseCode);

            return responseCode >= 200 && responseCode < 400;
        } catch (Exception e) {
            System.err.println("Error checking link: " + linkUrl);
            return false;
        }
    }

    @BeforeAll
    public static void setUp() {

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Ognjen\\Desktop\\svvt-lab-exam-prep-solutions-master\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("user-data-dir=C:\\Users\\Ognjen\\AppData\\Local\\Google\\Chrome\\User Data");
        options.addArguments("profile-directory=Profile 1");

        driver = new ChromeDriver(options);
        baseUrl = "https://store.steampowered.com/";
    }

    @Test
    public void testSidebarLinks() throws InterruptedException, IOException {

        driver.get(baseUrl);
        List<WebElement> sidebarLinks = driver.findElements(By.cssSelector(".home_page_gutter_block a"));


        for (WebElement link : sidebarLinks) {
            String href = link.getAttribute("href");

            if (href != null && !href.isEmpty()) {
                System.out.println("Testing link: " + href);

                boolean linkWorks = isLinkWorking(href);

                assert linkWorks : "The link is broken: " + href;
            } else {
                System.out.println("Skipping invalid or empty link.");
            }
        }
    }

    @Test
    public void testSupernavLinks() throws IOException {

        driver.get("https://store.steampowered.com/");
        List<WebElement> primaryMenuLinks = driver.findElements(By.cssSelector(".supernav_container .menuitem"));

        for (WebElement primaryLink : primaryMenuLinks) {
            String primaryHref = primaryLink.getAttribute("href");

            if (primaryHref != null && !primaryHref.isEmpty()) {
                System.out.println("Testing primary link: " + primaryHref);
                boolean primaryLinkWorks = isLinkWorking(primaryHref);
                assert primaryLinkWorks : "The primary link is broken: " + primaryHref;
            }

            String submenuSelector = primaryLink.getAttribute("data-tooltip-content");
            if (submenuSelector != null && !submenuSelector.isEmpty()) {
                List<WebElement> submenuLinks = driver.findElements(By.cssSelector(submenuSelector + " .submenuitem"));

                for (WebElement submenuLink : submenuLinks) {
                    String submenuHref = submenuLink.getAttribute("href");
                    if (submenuHref != null && !submenuHref.isEmpty()) {
                        System.out.println("Testing submenu link: " + submenuHref);
                        boolean submenuLinkWorks = isLinkWorking(submenuHref);
                        assert submenuLinkWorks : "The submenu link is broken: " + submenuHref;
                    }
                }
            }
        }
    }


    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
