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

public class AntyCaptcha_Exercise_2 {
    private static WebDriver driver;
    private String baseUrl = "https://antycaptcha.amberteam.pl/exercises/exercise2";

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
    }

    @Test
    public void test() {
        driver.get(baseUrl);

        // Get a text to be inputted
        WebElement textInputCode = driver.findElement(By.xpath("//table/tbody/tr[position()=2]/td[position()=2]/code[position()=1]"));
        String textInput = textInputCode.getText();

        // Get a text field to be pressed
        WebElement textFieldCode = driver.findElement(By.xpath("//table/tbody/tr[position()=2]/td[position()=2]/code[position()=2]"));
        String textField = textFieldCode.getText();

        // Get a button to be pressed
        WebElement buttonNameCode = driver.findElement(By.xpath("//table/tbody/tr[position()=3]/td[position()=2]/code"));
        String buttonName = buttonNameCode.getText();

        // Get a suggested trail from table
        WebElement expectedResultCode = driver.findElement(By.xpath("//table/tbody/tr[last()]/td[last()]/code"));
        String expectedTrail = expectedResultCode.getText();

        // Find, clear and fill text field
        WebElement inputField = driver.findElement(By.xpath("//*[@id=\""+textField.toLowerCase()+"\"]"));
        inputField.clear();
        inputField.sendKeys(textInput);

        // Find, and click proper submit button
        WebElement submitButton = driver.findElement(By.xpath("//button[contains(text(), '"+buttonName+"')]"));
        submitButton.click();

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

        Assertions.assertEquals(expectedTrail, actualTrail, "Received trail does not match one presented in table");
        Assertions.assertEquals("OK. Good answer", websiteAssertion, "Website assertion does not match expected one");
    }

    @AfterEach
    public void cleanUp() {
        driver.close();
    }


}
