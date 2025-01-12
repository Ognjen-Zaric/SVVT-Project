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

public class CommunityWorkshopTest {


    private static WebDriver driver;
    private static String baseUrl;

    @BeforeAll
    public static void setUp() {

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Ognjen\\Desktop\\svvt-lab-exam-prep-solutions-master\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("user-data-dir=C:\\Users\\Ognjen\\AppData\\Local\\Google\\Chrome\\User Data\\");
        options.addArguments("profile-directory=Profile 1");

        driver = new ChromeDriver(options);
        baseUrl = "https://store.steampowered.com/";
    }

    @Test
    public void testAddingWorkShopEntry() throws InterruptedException {

        driver.get(baseUrl);

        driver.navigate().to("https://steamcommunity.com/workshop/");

        driver.navigate().to("https://steamcommunity.com/sharedfiles/filedetails/?id=3070244462&searchtext=aim");

        Thread.sleep(1000);

        WebElement subscribeButton = driver.findElement(By.id("SubscribeItemOptionSubscribed"));
        String classAttribute = subscribeButton.getAttribute("class");

        if (classAttribute.contains("selected")) {
            fail("Already subscribed");

        } else {
            WebElement subscribe = driver.findElement(By.id("SubscribeItemBtn"));
            subscribe.click();
            Thread.sleep(3000);

            assert driver.findElement(By.id("JustSubscribed")).isDisplayed();
        }
    }


    @Test
    public void testWorkshopSearch() throws InterruptedException {

        driver.get(baseUrl);
        driver.navigate().to("https://steamcommunity.com/workshop/");
        Thread.sleep(1000);

        driver.findElement(By.id("workshopSearchText")).sendKeys("Counter-Strike 2", Keys.ENTER);
        assertEquals("https://steamcommunity.com/app/730/workshop/", driver.getCurrentUrl());

    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}


