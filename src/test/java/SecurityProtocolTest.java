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

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.*;

public class SecurityProtocolTest {


    private static WebDriver driver;
    private static String baseUrl;
    private static String unsecureUrl;

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Ognjen\\Desktop\\svvt-lab-exam-prep-solutions-master\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        baseUrl = "https://store.steampowered.com/";
        unsecureUrl = "http://store.steampowered.com/";
    }

    @Test
    public void testSSLCertificate() throws InterruptedException {

        driver.get(baseUrl);
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.startsWith("https"));

    }

    @Test
    public void testHTTPRedirectToHTTPS() {
        try {

            URL httpUrl = new URL(unsecureUrl);
            HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.connect();

            int responseCode = connection.getResponseCode();
            System.out.println("HTTP Response Code: " + responseCode);

            if (responseCode == 301 || responseCode == 302) {
                String location = connection.getHeaderField("Location");
                System.out.println("Redirected to: " + location);

                assertTrue( "Redirection is not to a secure HTTPS URL!" ,location.startsWith("https://"));
            } else {
                System.out.println("No redirection occurred. Response Code: " + responseCode);
                assertTrue("The site did not redirect to HTTPS!" ,false);
            }
        } catch (Exception e) {
            System.err.println("Error during HTTP to HTTPS test: " + e.getMessage());
            assertTrue("An error occurred during the test!", false);
        }
    }

    @Test
    public void testForClickjacking() throws IOException {
        String testHtmlPath = "test_clickjacking.html";
        createTestHtml(testHtmlPath, "https://store.steampowered.com/");

        driver.get("file:///" + testHtmlPath);

        try {
            WebElement iframe = driver.findElement(By.tagName("iframe"));
            driver.switchTo().frame(iframe);
            WebElement body = driver.findElement(By.tagName("body"));

            assertTrue("The website is vulnerable to Clickjacking!", body.isDisplayed());
            System.out.println("Clickjacking vulnerability detected!");
        } catch (Exception e) {
            System.out.println("Clickjacking protection is active.");
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    private static void createTestHtml(String filePath, String targetUrl) throws IOException {
        String htmlContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Clickjacking Test</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>Testing for Clickjacking</h1>\n" +
                "    <iframe src=\"" + targetUrl + "\" width=\"800\" height=\"600\"></iframe>\n" +
                "</body>\n" +
                "</html>";

        FileWriter writer = new FileWriter(filePath);
        writer.write(htmlContent);
        writer.close();
    }


    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
