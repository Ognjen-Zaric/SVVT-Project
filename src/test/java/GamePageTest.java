import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import static org.junit.Assert.*;

public class GamePageTest {

    JavascriptExecutor js = (JavascriptExecutor) driver;
    private static WebDriver driver;
    private static String baseUrl;

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
    public void testGamePage() throws InterruptedException {

        driver.get(baseUrl);
        Thread.sleep(1000);

        WebElement searchBox = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[2]/div[2]/div/div/div/div[2]/div[2]/div/div[9]/div[1]/form/div/input"));
        searchBox.sendKeys("Counter-Strike 2");
        searchBox.submit();

        WebElement result = driver.findElement(By.partialLinkText("Counter-Strike 2"));
        result.click();

        Thread.sleep(1000);

        assert driver.findElement(By.id("game_highlights")).isDisplayed();

        assert driver.findElement(By.id("game_area_description")).isDisplayed();

        assert driver.findElement(By.id("app_reviews_hash")).isDisplayed();
    }


    @Test
    public void testRelevantGames() throws InterruptedException {

        driver.get(baseUrl);
        Thread.sleep(1000);

        WebElement searchBox = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[2]/div[2]/div/div/div/div[2]/div[2]/div/div[9]/div[1]/form/div/input"));
        searchBox.sendKeys("Counter-Strike 2");
        searchBox.submit();

        WebElement result = driver.findElement(By.partialLinkText("Counter-Strike 2"));
        result.click();

        Thread.sleep(2000);
        assert driver.findElement(By.className("related_items_ctn")).isDisplayed();

        js.executeScript("window.scrollBy(0,4000)");
        Thread.sleep(2000);

        WebElement seeAllRelevantGames = driver.findElement(By.xpath("//span[contains(.,'See All')]"));

        seeAllRelevantGames.click();

        assert driver.getPageSource().contains("Looking for similar items");
    }

    @Test
    public void testReviews() throws InterruptedException {

        driver.get(baseUrl);
        Thread.sleep(1000);

        WebElement searchBox = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[2]/div[2]/div/div/div/div[2]/div[2]/div/div[9]/div[1]/form/div/input"));
        searchBox.sendKeys("Counter-Strike 2");
        searchBox.submit();

        WebElement result = driver.findElement(By.partialLinkText("Counter-Strike 2"));
        result.click();

        Thread.sleep(1000);

        js.executeScript("window.scrollBy(0,4200)");
        Thread.sleep(2000);

        WebElement seeNegativeReviews = driver.findElement(By.cssSelector("#review_type_positive"));
        seeNegativeReviews.click();
        assertEquals("Negative" ,driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[2]/div[1]/div[7]/div/div/div[5]/div[1]/div[3]")).getText());
        Thread.sleep(2000);

        WebElement seePositiveReviews = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[2]/div[1]/div[7]/div/div/div[4]/div[1]/div[2]/div/input[2]"));
        seePositiveReviews.click();
        assertEquals("Positive" ,driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[2]/div[1]/div[7]/div/div/div[5]/div[1]/div[3]")).getText());
        Thread.sleep(2000);

    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}


