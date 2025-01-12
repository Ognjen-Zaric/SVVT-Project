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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class InputValidationTest {


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
    public void testSearchForXSSVulnerability() throws InterruptedException {
        driver.get(baseUrl);

        WebElement searchBox = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[2]/div[2]/div/div/div/div[2]/div[2]/div/div[9]/div[1]/form/div/input"));

        String xssPayload = "<script>alert('XSS')</script>";
        searchBox.sendKeys(xssPayload);

        searchBox.submit();
        Thread.sleep(1000);

        try {
            assertFalse("XSS vulnerability detected! Alert box triggered." ,isAlertPresent());
        } catch (UnhandledAlertException e) {
            System.out.println("Alert triggered: Possible XSS vulnerability.");
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }



    @Test
    public void testSearchWithInvalidChars() {
        driver.get(baseUrl);

        WebElement searchBox = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[2]/div[2]/div/div/div/div[2]/div[2]/div/div[9]/div[1]/form/div/input"));
        searchBox.sendKeys("∏↑᷿ձжͰ˧ɸ¶®§");
        searchBox.submit();

        String currentUrl = driver.getCurrentUrl();
        assert currentUrl.endsWith("term=%E2%88%8F%E2%86%91%E1%B7%BF%D5%B1%D0%B6%CD%B0%CB%A7%C9%B8%C2%B6%C2%AE%C2%A7");
    }

    @Test
    public void testSearchWithTooManyChars() {
        driver.get(baseUrl);

        // The search box has a limit of 64 characters
        String tooManyChars = "iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii";
        int count = tooManyChars.length();
        System.out.println(count);

        WebElement searchBox = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[2]/div[2]/div/div/div/div[2]/div[2]/div/div[9]/div[1]/form/div/input"));
        searchBox.sendKeys(tooManyChars);
        searchBox.submit();

        String currentUrl = driver.getCurrentUrl();
        assert currentUrl.endsWith("term=iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");

    }

    @Test
    public void testSearchWithSpecialCharacters() {
        driver.get(baseUrl);

        WebElement searchBox = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[2]/div[2]/div/div/div/div[2]/div[2]/div/div[9]/div[1]/form/div/input"));
        searchBox.sendKeys("P@R!&L #`2");
        searchBox.submit();

        List<WebElement> results = driver.findElements(By.id("search_results"));
        assert !results.isEmpty();
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
