import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class FormSubmissionTest {
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
    public void testLoginWithEmptyDetails() throws InterruptedException {

        driver.get("https://steamcommunity.com/login/home/");
        Thread.sleep(1000);


        driver.findElement(By.cssSelector("button[type='submit']"))
                .click();

        Thread.sleep(1000);

        String errorMessage = driver.getPageSource();
        assertTrue(errorMessage.contains("Please check your password and account name and try again."));

    }

    @Test
    public void testRegisterWithEmptyDetails() throws InterruptedException {
        driver.get(baseUrl);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[4]/div[3]/div[2]/div/div/a[2]"))
                .click();

        Thread.sleep(1000);
        driver.findElement(By.id("i_agree_check")).click();

        WebElement createAccount = driver.findElement(By.id("createAccountButton"));
        createAccount.click();

        Thread.sleep(1000);

        WebElement errorWindow = driver.findElement(By.id("error_display"));
        assert errorWindow.isDisplayed();
        assert errorWindow.getText().contains("Please enter a valid email address.");
        assert errorWindow.getText().contains("Please fill in the Confirm email address field.");

    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
