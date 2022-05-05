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

public class AntyCaptcha_Exercise_4 {
    private static WebDriver driver;
    private String baseUrl = "https://antycaptcha.amberteam.pl/exercises/exercise4";

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
    }

    @Test
    public void test() {
        // Start
        driver.get(baseUrl);

        // Get a list of radio buttons required to be selected
        List<WebElement> buttonsList = driver.findElements(By.xpath("//table/tbody/tr[position()>1]/td[position()=2]/code"));

        // Get a suggested trail from table
        WebElement expectedResultCode = driver.findElement(By.xpath("//table/tbody/tr[last()]/td[last()]/code"));
        String expectedTrail = expectedResultCode.getText();

        //Execute list of radios to be clicked
        String numberString;
        String buttonText;
        for(int i = 0; i<buttonsList.size(); i++) {
            numberString = Integer.toString(i);
            System.out.println(numberString);
            buttonText = buttonsList.get(i).getText();

            WebElement givenRadio = driver.findElement(By.xpath("//div[h5[contains(text(), 'Group "+numberString+":')]]/text()[contains(., '"+buttonText+"')]/preceding-sibling::input[1]"));
            givenRadio.click();
        }

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

        // Assert if everything went ok
        Assertions.assertEquals(expectedTrail, actualTrail, "Received trail does not match one presented in table");
        Assertions.assertEquals("OK. Good answer", websiteAssertion, "Website assertion does not match expected one");
    }

    @AfterEach
    public void cleanUp() {
        driver.close();
    }
}
