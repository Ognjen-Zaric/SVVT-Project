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

import static org.junit.Assert.*;

public class UserLoginTest {

    public String realUsername = "";
    public String realPassword = "";
    public String fakePassword = "fakepassword";
    public String fakeUsername = "fakeusername";

    private static WebDriver driver;
    private static String baseUrl;

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Ognjen\\Desktop\\svvt-lab-exam-prep-solutions-master\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        baseUrl = "https://store.steampowered.com/";
    }

    @Test
    public void testLoginWithValidCredentials() throws InterruptedException {

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
        Thread.sleep(10000);

        WebElement accountPageLink = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[1]/div/div[3]/a"));
        assertTrue(accountPageLink.isDisplayed());

    }


    @Test
    public void testLoginWithIncorrectPassword() throws InterruptedException {
        driver.get(baseUrl);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[1]/div/div[3]/div/a[2]"))
                .click();

        Thread.sleep(1000);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[1]/div/div/div/div[2]/div/form/div[1]/input"))
                .sendKeys(realUsername);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[1]/div/div/div/div[2]/div/form/div[2]/input"))
                .sendKeys(fakePassword);

        driver.findElement(By.cssSelector("button[type='submit']"))
                .click();

        Thread.sleep(1000);

        String errorMessage = driver.getPageSource();
        assertTrue(errorMessage.contains("Please check your password and account name and try again."));
    }

    @Test
    public void testLoginWithUnregisteredUsername() throws InterruptedException {
        driver.get(baseUrl);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[1]/div/div[3]/div/a[2]"))
                .click();

        Thread.sleep(1000);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[1]/div/div/div/div[2]/div/form/div[1]/input"))
                .sendKeys(fakeUsername);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[1]/div/div/div/div[2]/div/form/div[2]/input"))
                .sendKeys(realUsername);

        driver.findElement(By.cssSelector("button[type='submit']"))
                .click();

        Thread.sleep(1000);

        String errorMessage = driver.getPageSource();
        assertTrue(errorMessage.contains("Please check your password and account name and try again."));

    }


    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
