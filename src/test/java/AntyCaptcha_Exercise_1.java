import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AntyCaptcha_Exercise_1 {
    private static WebDriver driver;
    private String baseUrl = "https://antycaptcha.amberteam.pl/exercises/exercise1";

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
    }

    @Test
    public void test() {
        driver.get(baseUrl);

        // Get a list of buttons required to be clicked
        List<WebElement> buttonsList = driver.findElements(By.xpath("//table/tbody/tr[position()>1]/td[position()=2]/code"));

        // Execute list
        String buttonName;
        for (WebElement webElement : buttonsList) {
            buttonName = webElement.getText();
            driver.findElement(By.xpath("//button[contains(text(), '" + buttonName + "')]")).click();
        }

        // Get a suggested trail from table
        WebElement expectedResultCode = driver.findElement(By.xpath("//table/tbody/tr[last()]/td[last()]/code"));
        String expectedTrail = expectedResultCode.getText();

        // Get an actual trail from code snippet
        WebElement actualResultCode = driver.findElement(By.xpath("//*[@id=\"trail\"]/code"));
        String actualTrail = actualResultCode.getText();

        // Find and click on "check result"
        driver.findElement(By.xpath("//*[@id=\"solution\"]")).click();

        // Wait until text changes, and get website answer
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(500));
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.xpath("//*[@id=\"trail\"]/code"), actualTrail)));
        actualResultCode = driver.findElement(By.xpath("//*[@id=\"trail\"]/code"));
        String websiteAssertion = actualResultCode.getText();

        Assertions.assertEquals(expectedTrail, actualTrail, "Received trail does not match one presented in table");
        Assertions.assertEquals("OK. Good answer", websiteAssertion, "Website assertion does not match expected one");
    }

    @AfterEach
    public void cleanUp() {
        driver.close();
    }


}
