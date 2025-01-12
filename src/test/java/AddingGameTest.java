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


import java.util.List;

import static org.junit.Assert.*;

public class AddingGameTest {


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
    public void testAddingGameToLibrary() throws InterruptedException {

        driver.get(baseUrl);

        driver.findElement(By.id("store_nav_search_term")).sendKeys("Destiny 2", Keys.ENTER);

        Thread.sleep(1000);

        WebElement game = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/form/div[1]/div/div[1]/div[3]/div/div[3]/a[1]"));
        game.click();

        Thread.sleep(1000);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[2]/div[1]/div[5]/div[2]/div[1]/div[1]/div[2]/div/div[3]/span")).click();
        Thread.sleep(1000);

        driver.navigate().refresh();

        List<WebElement> elements = driver.findElements(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[2]/div[1]/div[5]/div[2]/div[1]/div[1]/div[2]/div/div[3]/span"));

        assertTrue("The element should not exist", elements.isEmpty());

    }


    @Test
    public void testIfGameIsInLibrary() throws InterruptedException {

        driver.get(baseUrl);

        driver.findElement(By.id("account_pulldown")).click();
        driver.findElement(By.className("popup_menu_item")).click();

        String gameToCheck = "Destiny 2";
        boolean isGameFound = false;

        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[1]/div[3]/div/div[1]/div[2]/div[3]/div[1]/a")).click();

        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[5]/div[3]/div/div[4]/div[1]/input")).sendKeys(gameToCheck);

        Thread.sleep(1000);

        List<WebElement> gameTitles = driver.findElements(By.xpath("//span[@class='w6q9piMq3gT16oj_lEvpy']//a[@class='_22awlPiAoaZjQMqxJhp-KP']"));

        for (WebElement titleElement : gameTitles) {
            String gameTitle = titleElement.getText().trim();
            System.out.println("Found game title: " + gameTitle);

            if (gameTitle.equalsIgnoreCase(gameToCheck)) {
                isGameFound = true;
                System.out.println("Game found in library: " + gameTitle);
                break;
            }
        }

        assertTrue("The game '" + gameToCheck + "' was not found in the library.", isGameFound);
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}


