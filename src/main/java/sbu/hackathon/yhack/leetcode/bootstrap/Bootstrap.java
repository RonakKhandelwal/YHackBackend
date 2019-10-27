package sbu.hackathon.yhack.leetcode.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sbu.hackathon.yhack.leetcode.domain.Name;
import sbu.hackathon.yhack.leetcode.domain.Question;
import sbu.hackathon.yhack.leetcode.domain.User;
import sbu.hackathon.yhack.leetcode.repository.QuestionRepository;
import sbu.hackathon.yhack.leetcode.repository.UserRepository;
import sbu.hackathon.yhack.leetcode.scrapper.LeetcodeParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing Application...");
        long dataCount = questionRepository.count();
        log.info("{} records found", dataCount);
        if (dataCount == 0) {
            parseScrappedData();
        }
        if (userRepository.count() == 0) {
            loadInitialUsers();
        }
    }

    private Set<Question> sampleQuestions(List<Question> questionList) {
        Collections.shuffle(questionList);
        return new HashSet<>(questionList.subList(0, 5));
    }

    private void loadInitialUsers() {
        List<Question> questionList = questionRepository.findAll();

        File file = new File("/Users/mayanktiwari/Developer/SpringBootProjects/YHackBackend/src/main/resources/seed_data/users.csv");
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            try (InputStreamReader isr = new InputStreamReader(fileInputStream)) {
                try (BufferedReader bufferedReader = new BufferedReader(isr)) {
                    String line;
                    List<User> users = new ArrayList<>();
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] data = line.split(",", -1);
                        User user = new User();
                        user.setUserName(data[0]);
                        user.setPassword(data[1]);
                        user.setName(new Name(data[2], null, data[3]));
                        user.setEmail(data[4]);
                        user.setIsActive(true);
                        LocalDate currentDate = LocalDate.now();
                        user.setStartDate(currentDate);
                        user.setEndDate(currentDate.plus(2, ChronoUnit.YEARS));

                        user.setSolvedQuestions(sampleQuestions(questionList));

                        users.add(user);
                    }
                    log.info("Loaded {} users from the CSV file... Saving this information in the DB.", users.size());
                    userRepository.saveAll(users);
                    log.info("Finished creating user profiles...");
                }
            }
        } catch (Exception e) {
            log.error("Error parsing user information", e);
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
