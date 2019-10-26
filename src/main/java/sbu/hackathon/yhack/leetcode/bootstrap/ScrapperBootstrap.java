package sbu.hackathon.yhack.leetcode.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import sbu.hackathon.yhack.leetcode.config.SeleniumConfig;
import sbu.hackathon.yhack.leetcode.rest.LeetcodeBulkDataObject;
import sbu.hackathon.yhack.leetcode.rest.QuestionObject;
import sbu.hackathon.yhack.leetcode.rest.StatStatusPairObject;
import sbu.hackathon.yhack.leetcode.scrapper.QuestionScrapper;

import java.io.IOException;
import java.util.List;

/**
 * Created by Mayank Tiwari on 26/10/19.
 */
@Slf4j
//@Component
public class ScrapperBootstrap implements CommandLineRunner {

    @Autowired
    private QuestionScrapper scrapper;

    @Autowired
    private SeleniumConfig config;

    @Override
    public void run(String... args) throws Exception {
        log.info("Loaded scrapper bootstrap...");
    }

    private WebDriver initSeleniumDriver() {
        WebDriver webDriver = config.getDriver();
        webDriver.manage().window().maximize();
        return webDriver;
    }

    private void scrapeData() {
        WebDriver webDriver = initSeleniumDriver();
        LeetcodeBulkDataObject leetcodeBulkDataObject = scrapper.scrapeLeetcodeData();
        List<StatStatusPairObject> statStatusPairObjects = leetcodeBulkDataObject.getStatStatusPairObjects();
        log.info("Read {} questions data", statStatusPairObjects.size());
        final boolean[] skip = {true};
        statStatusPairObjects.forEach(statStatusPairObject -> {
            QuestionObject questionObject = statStatusPairObject.getQuestionObject();
            String questionTitle = questionObject.getQuestionTitle();
            if (questionTitle.equals("Encode String with Shortest Length")) {
                skip[0] = false;
            }
            if (!skip[0]) {
                try {
                    scrapper.downloadWebPageSelenium(questionObject.getQuestionTitleSlug(), webDriver);
                } catch (IOException e) {
                    log.error("Error scrapping data for file: " + questionTitle, e);
                }
            }
        });
    }
}
