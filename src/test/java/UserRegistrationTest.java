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


public class UserRegistrationTest {
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
    public void testRegisterWithValidDetails() throws InterruptedException {

        driver.get(baseUrl);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[4]/div[3]/div[2]/div/div/a[2]"))
                .click();

        Thread.sleep(1000);

        driver.findElement(By.id("email")).sendKeys("ognjen.zaric@stu.ibu.edu.ba");
        driver.findElement(By.id("reenter_email")).sendKeys("ognjen.zaric@stu.ibu.edu.ba");
        Select country = new Select(driver.findElement(By.id("country")));
        country.selectByValue("BA");
        driver.findElement(By.id("i_agree_check")).click();

        Thread.sleep(3000);

        WebElement confirmationWindow = driver.findElement(By.xpath("/html/body/div[5]"));
        assert confirmationWindow.isDisplayed();

    }

    @Test
    public void testRegisterWithExistingEmail() throws InterruptedException {
        driver.get(baseUrl);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[4]/div[3]/div[2]/div/div/a[2]"))
                .click();

        Thread.sleep(1000);

        driver.findElement(By.id("email")).sendKeys("ognjen.zaric64@gmail.com");
        driver.findElement(By.id("reenter_email")).sendKeys("ognjen.zaric64@gmail.com");
        Select country = new Select(driver.findElement(By.id("country")));
        country.selectByValue("BA");
        driver.findElement(By.id("i_agree_check")).click();
        WebElement createAccount = driver.findElement(By.id("createAccountButton"));
        createAccount.click();

        Thread.sleep(5000);

        WebElement inUse = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[1]/div[3]/div[1]"));
        assert inUse.isDisplayed();

    }


    @Test
    public void testRegisterWithoutAgreeingToTerms() throws InterruptedException {
        driver.get(baseUrl);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[4]/div[3]/div[2]/div/div/a[2]"))
                .click();

        Thread.sleep(1000);

        driver.findElement(By.id("email")).sendKeys("ognjen.zaric@stu.ibu.edu.ba");
        driver.findElement(By.id("reenter_email")).sendKeys("ognjen.zaric@stu.ibu.edu.ba");
        Select country = new Select(driver.findElement(By.id("country")));
        country.selectByValue("BA");
        driver.findElement(By.id("createAccountButton")).click();
        Thread.sleep(1000);
        WebElement errorWindow = driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[3]/div[1]/div[1]"));
        assert errorWindow.isDisplayed();

    }

    @Test
    public void testRegisterWithTwoAddreses() throws InterruptedException {
        driver.get(baseUrl);

        driver.findElement(By.xpath("/html/body/div[1]/div[7]/div[6]/div[4]/div[3]/div[2]/div/div/a[2]"))
                .click();

        Thread.sleep(1000);

        driver.findElement(By.id("email")).sendKeys("ognjen.zaric@stu.ibu.edu.ba");
        driver.findElement(By.id("reenter_email")).sendKeys("ognjen.zaric64@gmail.com");
        Select country = new Select(driver.findElement(By.id("country")));
        country.selectByValue("BA");
        driver.findElement(By.id("createAccountButton")).click();
        Thread.sleep(1000);

        WebElement errorWindow = driver.findElement(By.id("error_display"));
        assert errorWindow.isDisplayed();
        assert errorWindow.getText().contains("Please enter the same address in both email address fields.");

    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
