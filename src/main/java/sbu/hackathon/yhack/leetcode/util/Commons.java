package sbu.hackathon.yhack.leetcode.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Created by Mayank Tiwari on 11/12/16.
 */
@Slf4j
public class Commons {

    /**
     * Generate a random hex encoded string token of the specified length
     *
     * @param length
     * @return random hex string
     */
    public static synchronized String generateUniqueToken(Integer length) {
        byte random[] = new byte[length];
        Random randomGenerator = new Random();
        StringBuffer buffer = new StringBuffer();

        randomGenerator.nextBytes(random);

        for (int j = 0; j < random.length; j++) {
            byte b1 = (byte) ((random[j] & 0xf0) >> 4);
            byte b2 = (byte) (random[j] & 0x0f);
            if (b1 < 10)
                buffer.append((char) ('0' + b1));
            else
                buffer.append((char) ('A' + (b1 - 10)));
            if (b2 < 10)
                buffer.append((char) ('0' + b2));
            else
                buffer.append((char) ('A' + (b2 - 10)));
        }
        return (buffer.toString());
    }

    public static Map<String, Object> parseException(Exception e) {
        if (e == null) {
            return null;
        }
        Map<String, Object> exceptionMap = new HashMap<>();
        Throwable cause = e.getCause();
        Map<String, Object> causeMap = new HashMap<>();
        if (cause != null) {
            Throwable innerCause = cause.getCause();
            Map<String, Object> innerCauseMap = new HashMap<>();
            if (innerCause != null) {
                innerCauseMap.put("cause", null);
                innerCauseMap.put("message", innerCause.getMessage());
            }
            causeMap.put("cause", innerCauseMap);
            causeMap.put("message", cause.getMessage());
        }
        exceptionMap.put("cause", causeMap);
        exceptionMap.put("message", e.getMessage());

        return exceptionMap;
    }

    public static String listToStringWithSpaces(String... values) {
        if (values == null) {
            return "";
        }
        List<String> valueList = new ArrayList<>();

        for (String value : values) {
            if (!StringUtils.isEmpty(value)) {
                valueList.add(value.trim());
            }
        }
        return String.join(" ", valueList);
    }

    public static List initializeListIfEmpty(List list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public static boolean isServerReachable(String url) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            // note : you may also need
//            HttpURLConnection.setInstanceFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(url)
                    .openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            log.error("Exception while trying to check server status", e);
            return false;
        }
    }


    public static String trimUnderscores(String inputString) {
        if (inputString != null) {
            return inputString.replaceAll("_", " ");
        }
        return null;
    }


    public static void main(String[] args) {
        /*String uniqueToken = generateUniqueToken(4);
        System.out.println(uniqueToken);

        List l = null;
        System.out.println(l);
        l = initializeListIfEmpty(l);
        System.out.println(l);*/
    }
}
