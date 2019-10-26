package sbu.hackathon.yhack.leetcode.scrapper;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

/**
 * Created by Mayank Tiwari on 26/10/19.
 */
@Slf4j
public class LeetcodeParser {

    private void readSourceFiles() throws IOException {
        File directory = new File("cached-leetcode-files");
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.getName().contains(".html")) {
                parseData(file);
            }
        }
        log.info("{} files found!", files.length);
    }

    private void parseData(File file) throws IOException {
        String fileName = file.getName();
        log.info("Preparing to parse file: {}", fileName);

        Document document = Jsoup.parse(file, "utf-8");
        Elements descriptionElement = document.select(".description__24sA");
        log.info("{} elements found!", descriptionElement.size()); // TODO: 26/10/19 Check

        if (descriptionElement.isEmpty()) {
            throw new RuntimeException("Description not found for " + fileName);
        }

        Elements problemDescription = descriptionElement.select(".question-content__JfgR");
//        System.out.println(problemDescription.html());

        log.debug("Finished parsing file: {}", fileName);
    }

    public static void main(String[] args) throws IOException {
        LeetcodeParser leetcodeParser = new LeetcodeParser();
        leetcodeParser.readSourceFiles();
//        leetcodeParser.parseData(new File("/Users/mayanktiwari/Developer/SpringBootProjects/YHackBackend/cached-leetcode-files/two-sum.html"));
    }

}
