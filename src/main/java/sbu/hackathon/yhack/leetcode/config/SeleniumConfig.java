package sbu.hackathon.yhack.leetcode.config;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mayank Tiwari on 26/10/19.
 */
@Configuration
public class SeleniumConfig {

    private WebDriver driver;

    static {
        System.setProperty("webdriver.gecko.driver", findFile("geckodriver.mac"));
    }

    static private String findFile(String filename) {
        String paths[] = {"", "bin/", "target/classes"};
        for (String path : paths) {
            if (new File(path + filename).exists())
                return path + filename;
        }
        return "";
    }

    public SeleniumConfig() {
        Capabilities capabilities = DesiredCapabilities.firefox();
        driver = new FirefoxDriver(capabilities);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

}
