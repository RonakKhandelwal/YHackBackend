package sbu.hackathon.yhack.leetcode.scrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;
import sbu.hackathon.yhack.leetcode.rest.LeetcodeBulkData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.zip.GZIPInputStream;

/**
 * Created by Mayank Tiwari on 26/10/19.
 */
@Slf4j
@Component
public class QuestionScrapper {

    public LeetcodeBulkData scrapeLeetcodeData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File src = new File("/Users/mayanktiwari/Downloads/leetcode.json");
            LeetcodeBulkData leetcodeBulkData = objectMapper.readValue(src, LeetcodeBulkData.class);
            log.info("Read {} objects", leetcodeBulkData.getStatStatusPairs().size());
            return leetcodeBulkData;
        } catch (IOException e) {
            log.error("Error parsing Leetcode data", e);
        }
        return null;
    }

    public void downloadWebPage(String pageName) throws IOException {
        String url = MessageFormat.format("https://leetcode.com/problems/{0}/", pageName);
        log.info("Reading data from URL: {}", url);
        HttpGet httpget = new HttpGet(url);

        File outputDirectory = new File("cached-leetcode-files");
        if (!outputDirectory.exists()) {
            boolean mkDirs = outputDirectory.mkdirs();
            log.info("{} Finished creating output directory", mkDirs);
        }

        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = closeableHttpClient.execute(httpget)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
//                long len = entity.getContentLength();
//                log.info("Reading {} bytes of data", len);
//                System.out.println(EntityUtils.toString(entity));
                File outputFile = new File(outputDirectory, pageName + ".html");
                try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile, false)) {
                    entity.writeTo(fileOutputStream);
                    log.info("Finished writing output to location: {}", outputFile.getAbsolutePath());
                }
            } else {
                log.error("No data found!");
            }
        }
    }

    private void readURLData() throws Exception {
        //            LeetcodeBulkData leetcodeBulkData = objectMapper.readValue(new URL("/Users/mayanktiwari/Downloads/leetcode.json"), LeetcodeBulkData.class);
        String urlString = "https://leetcode.com/api/problems/all";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Host", "leetcode.com");
        conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:70.0) Gecko/20100101 Firefox/70.0");


        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        //Response handling
        InputStream responseBody = null;
        if (isGzipResponse(conn)) {
            responseBody = new GZIPInputStream(conn.getInputStream());
        } else {
            responseBody = conn.getInputStream();
        }
    }

    protected boolean isGzipResponse(HttpURLConnection con) {
        String encodingHeader = con.getHeaderField("Content-Encoding");
        return (encodingHeader != null && encodingHeader.toLowerCase().contains("gzip"));
    }

    public static void main(String[] args) throws Exception {
//        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
//        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
//        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "info");
//        Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.INFO);
//        Logger.getLogger("httpclient.wire.header").setLevel(Level.INFO);
//        Logger.getLogger("httpclient.wire.content").setLevel(Level.INFO);

        QuestionScrapper questionScrapper = new QuestionScrapper();
//        questionScrapper.scrapeQuestions();
        questionScrapper.downloadWebPage("two-sum");
    }

}
