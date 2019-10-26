package sbu.hackathon.yhack.leetcode.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mayank Tiwari on 26/10/19.
 */
@Configuration
public class SeleniumConfig {

    static {
        System.setProperty("webdriver.gecko.driver", findFile("geckodriver"));
        System.setProperty("webdriver.chrome.driver", findFile("chromedriver"));
    }

    static private String findFile(String filename) {
        String[] paths = {"", "bin/", "target/classes"};
        for (String path : paths) {
            if (new File(path + filename).exists())
                return path + filename;
        }
        return "";
    }

    @Bean
    public WebDriver getDriver() {
        //Capabilities capabilities = DesiredCapabilities.firefox();
        //driver = new FirefoxDriver(new FirefoxOptions(capabilities));
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        return driver;
    }


}
