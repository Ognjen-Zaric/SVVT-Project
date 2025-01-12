import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;
import java.util.List;

import static org.junit.Assert.*;

public class WishlistTest {


    private static WebDriver driver;
    private static String baseUrl;

    @BeforeAll
    public static void setUp() {

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Ognjen\\Desktop\\svvt-lab-exam-prep-solutions-master\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("user-data-dir=C:\\Users\\ognje\\AppData\\Local\\Google\\Chrome\\User Data\\");
        options.addArguments("profile-directory=Profile 1");

        driver = new ChromeDriver(options);
        baseUrl = "https://store.steampowered.com/";
    }

    @Test
    public void testAddGameToWishlist() throws InterruptedException {

        driver.get(baseUrl);

        driver.findElement(By.id("store_nav_search_term")).sendKeys("Hades II", Keys.ENTER);

        Thread.sleep(1000);

        WebElement game = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/form/div[1]/div/div[1]/div[3]/div/div[3]/a[1]"));
        game.click();

        Thread.sleep(3000);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        try {
            WebElement addToWishlistButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("#add_to_wishlist_area a.add_to_wishlist")));

            addToWishlistButton.click();
            System.out.println("Clicked 'Add to Wishlist' button!");

            WebElement wishlistSuccess = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("#add_to_wishlist_area_success")));
            assertTrue("Wishlist success state is not visible!", wishlistSuccess.isDisplayed());
            System.out.println("Game successfully added to the wishlist!");

        } catch (TimeoutException e) {
            try {
                WebElement alreadyWishlisted = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("#add_to_wishlist_area_success")));
                if (alreadyWishlisted.isDisplayed()) {
                    System.out.println("Game is already on the wishlist!");
                } else {
                    throw new RuntimeException("Unexpected error: Wishlist state could not be determined.");
                }
            } catch (TimeoutException ex) {
                throw new RuntimeException("Unexpected error: Wishlist state could not be determined.", ex);
            }
        }
    }


    @Test
    public void testViewWishList() throws InterruptedException {
        driver.get(baseUrl);

        Thread.sleep(1000);

        WebElement wishlistButton = driver.findElement(By.id("wishlist_link"));
        wishlistButton.click();

        Thread.sleep(1000);

        List<WebElement> wishlistItems = driver.findElements(By.xpath("//div[contains(@class, 'Panel') and @data-index]"));
        String gameToCheck = "Portal 3";
        boolean gameFound = false;

        for (WebElement item : wishlistItems) {
            try {
                WebElement titleElement = item.findElement(By.xpath(".//a[@class='Fuz2JeT4RfI-']"));
                String gameTitle = titleElement.getText();

                if (gameTitle.equalsIgnoreCase(gameToCheck)) {
                    System.out.println("Game found in the wishlist: " + gameTitle);
                    gameFound = true;
                    break;
                }
            } catch (NoSuchElementException e) {
                System.out.println("Title element not found in one of the wishlist items.");
            }
        }

        assertTrue("The game '" + gameToCheck + "' was not found in the wishlist.", gameFound);

    }

    @Test
    public void testRemoveGameFromWishlist() throws InterruptedException {
        driver.get(baseUrl);

        Thread.sleep(1000);

        WebElement wishlistButton = driver.findElement(By.id("wishlist_link"));
        wishlistButton.click();

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
                boolean stillInWishlist = updatedGameTitles.stream()
                        .anyMatch(element -> element.getText().equalsIgnoreCase(gameTitleToRemove));

                assertFalse("The game was not removed from the wishlist!", stillInWishlist);
                break;
            }
        }

        if (!gameFound) {
            fail("The game '" + gameTitleToRemove + "' was not found in the wishlist.");
        }
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}



