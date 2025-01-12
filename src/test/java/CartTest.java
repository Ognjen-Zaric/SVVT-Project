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

public class CartTest {


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
    public void testAddGameToCart() throws InterruptedException {

        driver.get(baseUrl);

        driver.findElement(By.id("store_nav_search_term")).sendKeys("Hades II", Keys.ENTER);

        Thread.sleep(1000);

        WebElement game = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/form/div[1]/div/div[1]/div[3]/div/div[3]/a[1]"));
        game.click();

        Thread.sleep(1000);

        driver.findElement(By.className("btn_addtocart")).click();
        Thread.sleep(1000);

        WebElement cart = driver.findElement(By.className("DialogContent_InnerWidth"));
        assertTrue(cart.getText().contains("Added to your cart!"));

    }


    @Test
    public void addGameAlreadyInCart() throws InterruptedException {
        driver.get(baseUrl);

        driver.findElement(By.id("store_nav_search_term")).sendKeys("Hades", Keys.ENTER);

        Thread.sleep(1000);

        WebElement game = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/form/div[1]/div/div[1]/div[3]/div/div[3]/a[1]"));
        game.click();

        Thread.sleep(1000);

        driver.findElement(By.className("btn_addtocart")).click();
        Thread.sleep(1000);

        String currentUrl = driver.getCurrentUrl();
        if (!currentUrl.equals("https://store.steampowered.com/cart/")){
            fail("The game was not in the cart");
        }

    }

    @Test
    public void testRemoveGameFromCart() throws InterruptedException {
        driver.get(baseUrl);

        Thread.sleep(1000);

        assert (driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[2]/div[2]/div/div/div/div[1]/div/div[2]/div/a"))).isDisplayed();

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[2]/div[2]/div/div/div/div[1]/div/div[2]/div/a")).click();


        Thread.sleep(1000);

        String gameTitleToRemove = "Sonic Colors: Ultimate";
        boolean gameFound = false;

        List<WebElement> gameTitles = driver.findElements(By.xpath("//div[@class='pMrnNJp5sDA-']/a"));

        for (WebElement gameTitleElement : gameTitles) {
            String gameTitle = gameTitleElement.getText();
            if (gameTitle.equalsIgnoreCase(gameTitleToRemove)) {
                gameFound = true;
                System.out.println("Game found: " + gameTitle);

                WebElement parentContainer = gameTitleElement.findElement(By.xpath("./ancestor::div[@data-index]"));
                WebElement removeButton = parentContainer.findElement(By.xpath(".//button[contains(text(), 'remove')]"));

                removeButton.click();
                System.out.println("Clicked remove button for: " + gameTitleToRemove);

                Thread.sleep(2000);

                List<WebElement> updatedGameTitles = driver.findElements(By.xpath("//div[@class='pMrnNJp5sDA-']/a"));
                boolean stillInCart = updatedGameTitles.stream()
                        .anyMatch(element -> element.getText().equalsIgnoreCase(gameTitleToRemove));

                assertFalse("The game was not removed from the cart!", stillInCart);
                break;
            }
        }

        if (!gameFound) {
            fail("The game '" + gameTitleToRemove + "' was not found in the cart.");
        }
    }



    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}



