package sbu.hackathon.yhack.leetcode.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mayank Tiwari on 26/10/19.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LeetcodeBulkData implements Serializable {

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("num_solved")
    private int noQuestionSolved;

    @JsonProperty("num_total")
    private int totalQuestions;

    @JsonProperty("ac_easy")
    private int noEasyQuestionsSolved;

    @JsonProperty("ac_medium")
    private int noMediumQuestionsSolved;

    @JsonProperty("ac_hard")
    private int noHardQuestionsSolved;

    @JsonProperty("frequency_high")
    private int highFrequency;

    @JsonProperty("frequency_mid")
    private int midFrequency;

    @JsonProperty("category_slug")
    private String categorySlug;

    @JsonProperty("stat_status_pairs")
    private List<StatStatusPair> statStatusPairs;

}
