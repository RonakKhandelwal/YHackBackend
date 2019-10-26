package sbu.hackathon.yhack.leetcode.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sbu.hackathon.yhack.leetcode.model.Question;
import sbu.hackathon.yhack.leetcode.repository.QuestionRepository;
import sbu.hackathon.yhack.leetcode.scrapper.LeetcodeParser;

import java.util.List;

/**
 * Created by Mayank Tiwari on 26/10/19.
 */
@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    @Autowired
    private LeetcodeParser leetcodeParser;

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing Application...");
        long dataCount = questionRepository.count();
        log.info("{} records found", dataCount);
        if (dataCount == 0) {
//        scrapeData();
            parseScrappedData();
        }
    }

    private void parseScrappedData() throws Exception {
        log.info("Preparing to parse all cached data...");
        List<Question> cachedData = leetcodeParser.parseAllCachedData();
        log.info("Parsed {} records... Preparing to Bulk insert data in DB...", cachedData.size());
        questionRepository.saveAll(cachedData);
        log.info("Finished writing data in DB...");
    }

}
