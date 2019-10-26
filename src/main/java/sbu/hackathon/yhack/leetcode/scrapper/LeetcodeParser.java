package sbu.hackathon.yhack.leetcode.scrapper;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import sbu.hackathon.yhack.leetcode.model.CompanyQuestion;
import sbu.hackathon.yhack.leetcode.model.Question;
import sbu.hackathon.yhack.leetcode.model.Topic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Mayank Tiwari on 26/10/19.
 */
@Slf4j
@Component
public class LeetcodeParser {

    public List<Question> parseAllCachedData() throws IOException {
        List<Question> parsedQuestions = new ArrayList<>();
        File directory = new File("cached-leetcode-files");
        File[] files = directory.listFiles();
        if (files == null) {
            return parsedQuestions;
        }
        for (File file : files) {
            if (file.getName().contains(".html")) {
                parsedQuestions.add(parseData(file));
            }
        }
        log.info("{} files found!", files.length);
        return parsedQuestions;
    }

    private int parseQuestion(Elements elements) {
        String data = elements.text().trim().replace(",", "");
        if (data.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(data);
    }

    private Question parseData(File file) throws IOException {
        Question question = new Question();

        String fileName = file.getName();
        log.trace("Preparing to parse file: {}", fileName);

        Document document = Jsoup.parse(file, "utf-8");
        Elements descriptionElement = document.select(".description__24sA");
        assertNonNull(descriptionElement, "Description", fileName);
//        question.setDescription(descriptionElement.html());

        Elements likedElement = descriptionElement.select("div.css-101rr4k > div.css-10o4wqw > button:nth-child(2) > span");
        assertNonNull(likedElement, "Up Vote", fileName);
//        log.trace("Up Votes: {}", likedElement.text());
        question.setUpVoteCount(parseQuestion(likedElement));

        Elements dislikedElement = descriptionElement.select("div.css-101rr4k > div.css-10o4wqw > button:nth-child(3) > span");
        assertNonNull(dislikedElement, "Down Voted", fileName);
//        log.trace("Down Votes: {}", dislikedElement.text());
        question.setDownVoteCount(parseQuestion(dislikedElement));

        Elements problemDescription = descriptionElement.select(".question-content__JfgR");
        assertNonNull(problemDescription, "Problem Description", fileName);
        log.trace("{} bytes of Problem description data found.", problemDescription.text().length());
        question.setDescription(problemDescription.html());

        Elements acceptedElement = descriptionElement.select("div > div:nth-child(3) > div.css-12aggky > div:nth-child(1) > div.css-jkjiwi");
        assertNonNull(acceptedElement, "Accepted", fileName);
//        log.info("\tAccepted: {}", acceptedElement.text());
        question.setAcceptedCount(parseQuestion(acceptedElement));

        Elements submissionElement = descriptionElement.select("div > div:nth-child(3) > div.css-12aggky > div:nth-child(2) > div.css-jkjiwi");
        assertNonNull(submissionElement, "Submissions", fileName);
//        log.info("\tSubmissions: {}", submissionElement.text());
        question.setSubmissionsCount(parseQuestion(submissionElement));

        Elements frequency = descriptionElement.select("button.time-period-button__3neY.selected__1jx3");
//        assertNonNull(frequency, "Frequency", fileName); //Optional Element
//        log.info("\tFrequency: {}", frequency.text());
        question.setQuestionFrequency(frequency.text());

        Set<CompanyQuestion> companyQuestions = question.getCompanyQuestions();
        Elements companyElementWrapper = descriptionElement.select("div.company-tag-wrapper__1rBy .btn-content-container__214G");
        if (!companyElementWrapper.isEmpty()) { //Optional Element
//            log.info("Found link with {} companies", companyElementWrapper.size());
            companyElementWrapper.listIterator().forEachRemaining(childCompanyElement -> {
                Elements companyElement = childCompanyElement.select(".btn-content__lOBM");
                assertNonNull(companyElement, "Company", fileName);

                Elements questionFrequency = childCompanyElement.select("div > span:nth-child(3)");
                assertNonNull(questionFrequency, "Company Question Frequency", fileName);
//                log.info("\t\tCompany: {} - {}", companyElement.text(), questionFrequency.text());
                companyQuestions.add(new CompanyQuestion(companyElement.text(), parseQuestion(questionFrequency)));
            });
        }

        Set<Topic> relatedTopics = question.getRelatedTopics();
        Elements topicElements = descriptionElement.select(".topic-tag__1jni");
        if (!topicElements.isEmpty()) { //Optional Element
            topicElements.listIterator().forEachRemaining(topicElement -> {
                assertNonNull(topicElement, "Topic", fileName);
//                log.info("\t\tTopic: {}", topicElement.text());
                relatedTopics.add(new Topic(topicElement.text()));
            });
        }

        /*Elements hintElements = descriptionElement.select(".css-1s68uhd");
        if (!hintElements.isEmpty()) {
            hintElements.listIterator().forEachRemaining(hintElement -> {
                log.info("\t\tHint: {}", hintElement.text());
            });
        }*/

        log.trace("Finished parsing file: {}", fileName);
        return question;
    }

    private void assertNonNull(Element element, String message, String fileName) {
        if (element == null) {
            throw new RuntimeException(message + " element not found for file: " + fileName);
        }
    }

    private void assertNonNull(Elements elements, String message, String fileName) {
        if (elements == null || elements.isEmpty()) {
            throw new RuntimeException(message + " element not found for file: " + fileName);
        }
    }

    public static void main(String[] args) throws IOException {
        LeetcodeParser leetcodeParser = new LeetcodeParser();
        List<Question> questions = leetcodeParser.parseAllCachedData();
        for (Question question : questions) {
            System.out.println(question);
        }
//        leetcodeParser.parseData(new File("/Users/mayanktiwari/Developer/SpringBootProjects/YHackBackend/cached-leetcode-files/two-sum.html"));
//        leetcodeParser.parseData(new File("/Users/mayanktiwari/Developer/SpringBootProjects/YHackBackend/cached-leetcode-files/smallest-string-with-swaps.html"));
//        leetcodeParser.parseData(new File("/Users/mayanktiwari/Developer/SpringBootProjects/YHackBackend/cached-leetcode-files/nth-magical-number.html"));
    }

}
