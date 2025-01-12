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

public class GameSaleTest {

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
    public void testIfGameIsOnSale() throws InterruptedException {

        driver.get("https://store.steampowered.com/app/367520/Hollow_Knight/");

        boolean isOnSale = false;
        WebElement gameContainer = driver.findElement(By.xpath("//div[contains(@class, 'game_purchase_action')]"));

        try {
            if (gameContainer.findElement(By.className("discount_pct")).isDisplayed()) {
                isOnSale = true;
            }
        }
        catch (NoSuchElementException e) {
            System.out.println("Game is not on sale.");
        }

        assertTrue("The game is not on sale as expected.", isOnSale);

    }


    @Test
    public void testIfSalePriceIsCorrect() throws InterruptedException {

        driver.get("https://store.steampowered.com/app/367520/Hollow_Knight/");

        boolean isPriceCorrect = false;
        WebElement gameContainer = driver.findElement(By.xpath("//div[contains(@class, 'game_purchase_action')]"));
        WebElement discountBlock = gameContainer.findElement(By.className("discount_block"));

        String discountPercentageText = discountBlock.findElement(By.className("discount_pct")).getText();
        String originalPriceText = discountBlock.findElement(By.className("discount_original_price")).getText();
        String finalPriceText = discountBlock.findElement(By.className("discount_final_price")).getText();

        double discountPercentage = Double.parseDouble(discountPercentageText.replaceAll("[^\\d]", "")) / 100.0;
        double originalPrice = Double.parseDouble(originalPriceText.replace(",", ".").replaceAll("[^\\d.]", ""));
        double finalPrice = Double.parseDouble(finalPriceText.replace(",", ".").replaceAll("[^\\d.]", ""));

        double expectedPrice = originalPrice * (1 - discountPercentage);
        expectedPrice = Math.round(expectedPrice * 100.0) / 100.0;

        System.out.println("Original Price: " + originalPrice);
        System.out.println("Discount Percentage: " + discountPercentage);
        System.out.println("Displayed Final Price: " + finalPrice);
        System.out.println("Expected Final Price: " + expectedPrice);


        isPriceCorrect = (finalPrice == expectedPrice);

        assertTrue("The discounted price is not correctly calculated or displayed.", isPriceCorrect);
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}


