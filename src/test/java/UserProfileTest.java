import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import java.util.List;

import static org.junit.Assert.*;

public class UserProfileTest {


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
    public void testDisplayNameUpdate() throws InterruptedException {

        driver.get(baseUrl);

        driver.findElement(By.id("account_pulldown")).click();

        driver.findElement(By.className("popup_menu_item")).click();

        Thread.sleep(1000);


        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[1]/div[2]/div/div/div/div[3]/div[2]/a")).click();

        WebElement displayName = driver.findElement(By.name("personaName"));
        displayName.clear();

        String newDisplayName = "TheLegend27";
        displayName.sendKeys(newDisplayName);


        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[4]/div/div[2]/div/div/div[3]/div[3]/div[2]/form/div[7]/button[1]")).click();

        Thread.sleep(1000);


        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[4]/div/div[2]/div/div/div[3]/div[2]/a")).click();

        WebElement updatedDisplayName = driver.findElement(By.className("actual_persona_name"));

        assert updatedDisplayName.getText().equals(newDisplayName);

    }


    @Test
    public void testInvalidDisplayName() throws InterruptedException {

        driver.get(baseUrl);

        driver.findElement(By.id("account_pulldown")).click();

        driver.findElement(By.className("popup_menu_item")).click();

        Thread.sleep(1000);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[1]/div[2]/div/div/div/div[3]/div[2]/a")).click();

        WebElement displayName = driver.findElement(By.name("personaName"));
        displayName.clear();

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[4]/div/div[2]/div/div/div[3]/div[3]/div[2]/form/div[7]/button[1]")).click();

        Thread.sleep(1000);

        assertTrue(driver.getPageSource().contains("Error"));
    }

    @Test
    public void testChangeCustomURL() throws InterruptedException {

        driver.get(baseUrl);

        driver.findElement(By.id("account_pulldown")).click();
        driver.findElement(By.className("popup_menu_item")).click();

        Thread.sleep(1000);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[1]/div[2]/div/div/div/div[3]/div[2]/a")).click();

        WebElement customURL = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[4]/div/div[2]/div/div/div[3]/div[3]/div[2]/form/div[3]/div[2]/div[3]/label/div[2]/input"));
        customURL.clear();
        String newURL = "SVVT-Rules";
        customURL.sendKeys(newURL);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[4]/div/div[2]/div/div/div[3]/div[3]/div[2]/form/div[7]/button[1]")).click();

        WebElement updatedCustomURL = driver.findElement(By.className("DialogLabelExplainer"));
        assert updatedCustomURL.getText().equals("Your profile will be available at: https://steamcommunity.com/id/" + newURL + "/");

    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}



