import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AntyCaptcha_Exercise_3 {
    private static WebDriver driver;
    private String baseUrl = "https://antycaptcha.amberteam.pl/exercises/exercise3";

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
    }

    @Test
    public void test() {
        // Start
        driver.get(baseUrl);

        // Get a field to be selected
        WebElement selectionCode = driver.findElement(By.xpath("//table/tbody/tr[2]/td[2]/code"));
        String selection = selectionCode.getText();

        // Get a suggested trail from table
        WebElement expectedResultCode = driver.findElement(By.xpath("//table/tbody/tr[2]/td[3]/code"));
        String expectedTrail = expectedResultCode.getText();

        // Find drop-down
        WebElement dropDown = driver.findElement(By.xpath("//*[@id=\"s13\"]"));

        // Select
        Select option = new Select(dropDown);
        option.selectByVisibleText(selection);

        // Get an actual trail from code snippet
        WebElement actualResultCode = driver.findElement(By.xpath("//*[@id=\"trail\"]/code"));
        String actualTrail = actualResultCode.getText();

        // Find and click on "check result"
        driver.findElement(By.xpath("//*[@id=\"solution\"]")).click();

        // Wait until text in code snippet changes, and get website answer
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(500));
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.xpath("//*[@id=\"trail\"]/code"), actualTrail)));
        actualResultCode = driver.findElement(By.xpath("//*[@id=\"trail\"]/code"));
        String websiteAssertion = actualResultCode.getText();

        // Assert if everything went ok
        Assertions.assertEquals(expectedTrail, actualTrail, "Received trail does not match one presented in table");
        Assertions.assertEquals("OK. Good answer", websiteAssertion, "Website assertion does not match expected one");
    }

    @AfterEach
    public void cleanUp() {
        driver.close();
    }


}
