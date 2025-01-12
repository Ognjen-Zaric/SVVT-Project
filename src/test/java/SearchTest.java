import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.List;

import static org.junit.Assert.*;


public class SearchTest {

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
    public void testSearchWithValidTitle() {
        driver.get(baseUrl);

        WebElement searchBox = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[2]/div[2]/div/div/div/div[2]/div[2]/div/div[9]/div[1]/form/div/input"));
        searchBox.sendKeys("Portal 2");
        searchBox.submit();

        WebElement result = driver.findElement(By.partialLinkText("Portal 2"));
        assert result.isDisplayed();
    }

    @Test
    public void testSearchWithPartialTitle() {
        driver.get(baseUrl);

        WebElement searchBox = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[2]/div[2]/div/div/div/div[2]/div[2]/div/div[9]/div[1]/form/div/input"));
        searchBox.sendKeys("Portal");
        searchBox.submit();

        List<WebElement> results = driver.findElements(By.id("search_results"));
        assert !results.isEmpty();
    }

    @Test
    public void testSearchWithInvalidTitle() {
        driver.get(baseUrl);

        WebElement searchBox = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[2]/div[2]/div/div/div/div[2]/div[2]/div/div[9]/div[1]/form/div/input"));
        searchBox.sendKeys("adsljashfkajhf");
        searchBox.submit();

        WebElement noResultsMessage = driver.findElement(By.className("search_results_count"));
        String message = noResultsMessage.getText();
        assertEquals("0 results match your search.", message);
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
