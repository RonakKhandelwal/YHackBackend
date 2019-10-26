package sbu.hackathon.yhack.leetcode.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sbu.hackathon.yhack.leetcode.rest.LeetcodeBulkData;
import sbu.hackathon.yhack.leetcode.rest.Question;
import sbu.hackathon.yhack.leetcode.rest.StatStatusPair;
import sbu.hackathon.yhack.leetcode.scrapper.QuestionScrapper;

import java.io.IOException;
import java.util.List;

/**
 * Created by Mayank Tiwari on 26/10/19.
 */
@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    @Autowired
    private QuestionScrapper scrapper;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing Application...");
//        scrapeData();
        scrapper.downloadWebPage("two-sum");
    }

    private void scrapeData() {
        LeetcodeBulkData leetcodeBulkData = scrapper.scrapeLeetcodeData();
        List<StatStatusPair> statStatusPairs = leetcodeBulkData.getStatStatusPairs();
        log.info("Read {} questions data", statStatusPairs.size());
        statStatusPairs.parallelStream()
                .forEach(statStatusPair -> {
                    Question question = statStatusPair.getQuestion();
                    String questionTitle = question.getQuestionTitle();
                    try {
                        scrapper.downloadWebPage(question.getQuestionTitleSlug());
                    } catch (IOException e) {
                        log.error("Error scrapping data for file: " + questionTitle, e);
                    }
                });
    }
}
