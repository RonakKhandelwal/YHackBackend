package sbu.hackathon.yhack.leetcode.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by Mayank Tiwari on 26/10/19.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StatStatusPairObject implements Serializable {

    @JsonProperty("stat")
    private QuestionObject questionObject;

    private String status;
    private int frequency;
    private int progress;

    @JsonProperty("difficulty")
    private Difficulty difficulty;

    @JsonProperty("paid_only")
    private boolean isPaidOnly;

    @JsonProperty("is_favor")
    private boolean isFavorite;

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private static class Difficulty {

        private int level;

    }

}
