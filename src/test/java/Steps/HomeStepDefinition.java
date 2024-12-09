package Steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class HomeStepDefinition {

    public static WebDriver driver;

    @Given("User navigates to Home Page")
    public void user_navigates_to_home_page() {
        System.setProperty("webdriver.driver.chrome", "C:\\Drivers\\WebDrivers\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        driver = new ChromeDriver(options);
        driver.get("https://reqres.in/");
    }

    @Then("Verify API Details")
    public void verifyAPIDetails() {

        driver.findElement(By.xpath("//a[@href='/api/unknown/23']")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6));
        wait.until(visibilityOfElementLocated(By.xpath("//a[contains(@data-key,\"request-output-link\")]")));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    @Then("Navigate to Support Page")
    public void navigateToSupportPage() {
        try {
            driver.findElement(By.xpath("//a[@href='#support-heading']")).click();
            Thread.sleep(6000);

        } catch (Exception e) {
            System.out.println("Support Link Doesn't Exists");

        }
        driver.close();
    }

    @Given("Call To GET API")
    public void callToGETAPI() {
        System.setProperty("webdriver.driver.chrome", "C:\\Drivers\\WebDrivers\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        driver = new ChromeDriver(options);
        driver.get("https://reqres.in/");
        try {
            driver.findElement(By.xpath("//a[contains(@href,'api/users/2')]")).click();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6));
            wait.until(visibilityOfElementLocated(By.xpath("//span[text()='/api/users/2']")));

        } catch (Exception e) {
            System.out.println("GET API Call Failed");

        }
    }

    @Then("Verify GetResponse")
    public void verifyGetResponse() {
        //Verifying the Status Code of API = 200
        try {
            Assert.assertTrue(Objects.requireNonNull(driver.getPageSource()).contains("200"));
            Thread.sleep(5000);
            //Verifying the sample response fields
            driver.findElement(By.xpath("//pre[contains(text(),'id')]"));
            driver.findElement(By.xpath("//a[contains(text(),'contentcaddy')]"));
        } catch (Exception e) {
            System.out.println("Response Code isn't 200:Error");

        }
        driver.close();
    }

    @Given("Call To POST API")
    public void callToPOSTAPI() {
        System.setProperty("webdriver.driver.chrome", "C:\\Drivers\\WebDrivers\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        driver = new ChromeDriver(options);
        driver.get("https://reqres.in/");
        try {
            driver.findElement(By.xpath("//a[contains(text(),'Create')]")).click();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6));
            wait.until(visibilityOfElementLocated(By.xpath("//span[text()='/api/users']")));

        } catch (Exception e) {
            System.out.println("POST API Call Failed");

        }

    }

    @Then("Verify PostResponse")
    public void verifyPostResponse() {
        //Verifying the Status Code of API = 201
        try {
            Assert.assertTrue(Objects.requireNonNull(driver.getPageSource()).contains("201"));
            Thread.sleep(5000);
            //Verifying the sample response fields
            driver.findElement(By.xpath("//pre[contains(text(),'name')]"));
            driver.findElement(By.xpath("//pre[contains(text(),'id')]"));
        } catch (Exception e) {
            System.out.println("Response Code isn't 201:Error");

        }
        driver.close();
    }

    private final CSVFiles processor = new CSVFiles();
    private List<String[]> outputData;
    private Map<String, String[]> file1Data;
    private Map<String, String[]> file2Data;

    private String getResourcePath(String fileName) throws URISyntaxException {
        Path path = null;
        try {
            path = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return path.toString();
    }

    @Given("Read CSV files {string} and {string}")
    public void readCSVFilesAnd(String file1, String file2) throws IOException, URISyntaxException {
        String file1Path = getResourcePath(file1);  // e.g., "src/test/resources/File1.csv"
        String file2Path = getResourcePath(file2);
        file1Data = processor.readFile(file1Path);
        file2Data = processor.readFile(file2Path);
    }

    @When("Calculate Price as Quantity * UnitPrice")
    public void calculatePriceAsQuantityUnitPrice() {
        outputData = processor.mergeFiles(file1Data, file2Data);
    }

    @Then("Output CSV file {string}")
    public void outputCSVFile(String outputFileName) throws IOException {
        processor.writeOutputCSV(outputFileName, outputData);
        // Optionally, you can verify the output file here as well
        System.out.println("Output file generated: " + outputFileName);
    }
}
