import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SessionHandlingTest {

    private static WebDriver driver;
    private static String baseUrl = "https://store.steampowered.com/";
    private static Set<Cookie> cookies;

    public String realUsername = "blazintoaddamlg1";
    public String realPassword = "Zc^U4gmXg&QgRA9D2nZ%qNnLx@iHkU";

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Ognjen\\Desktop\\svvt-lab-exam-prep-solutions-master\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
    }

    @Test
    public void testRememberMeFunctionality() throws InterruptedException {
        driver.get(baseUrl);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[1]/div/div[3]/div/a[2]"))
                .click();

        Thread.sleep(1000);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[1]/div/div/div/div[2]/div/form/div[1]/input"))
                .sendKeys(realUsername);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[1]/div/div/div/div[2]/div/form/div[2]/input"))
                .sendKeys(realPassword);

        driver.findElement(By.cssSelector("button[type='submit']"))
                .click();

        //mobile authenticator buffer
        Thread.sleep(1000);


        WebElement userProfile = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[1]/div/div[3]/a"));
        assertTrue(userProfile.isDisplayed(), "Login failed!");

        cookies = driver.manage().getCookies();
        System.out.println("Cookies saved: " + cookies);

        driver.quit();

        setUp();
        driver.get(baseUrl);

        for (Cookie cookie : cookies) {
            driver.manage().addCookie(cookie);
        }


        driver.navigate().refresh();


        WebElement restoredUserProfile = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[1]/div/div[3]/a"));
        assertTrue(restoredUserProfile.isDisplayed(), "User is not logged in after reopening the browser!");
    }

    @Test
    public void testExpirationRedirect() throws InterruptedException {
        driver.get(baseUrl);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[1]/div/div[3]/div/a[2]"))
                .click();

        Thread.sleep(1000);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[1]/div/div/div/div[2]/div/form/div[1]/input"))
                .sendKeys(realUsername);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[1]/div/div/div/div[2]/div/form/div[2]/input"))
                .sendKeys(realPassword);

        driver.findElement(By.cssSelector("button[type='submit']"))
                .click();

        //mobile authenticator buffer
        Thread.sleep(1000);



        WebElement userProfile = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[1]/div/div[3]/a"));
        assertTrue(userProfile.isDisplayed(), "Login failed!");


        driver.quit();


        setUp();
        driver.get(baseUrl);

        driver.navigate().to("https://steamcommunity.com/id/sejda2cancelled/edit/info");

        assertTrue(driver.getCurrentUrl().contains("login"), "User is not redirected to login page after expiration!");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
