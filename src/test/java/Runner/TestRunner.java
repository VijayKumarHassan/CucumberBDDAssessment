package Runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/Feature",
        glue = "Steps",
        monochrome = true,
        plugin = {"pretty", "html:target/cucumber-reports/report.html"},
        publish = true

)

public class TestRunner {
}
