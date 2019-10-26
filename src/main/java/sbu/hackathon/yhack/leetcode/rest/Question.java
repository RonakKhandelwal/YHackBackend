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
public class Question implements Serializable {

    @JsonProperty("question_id")
    private Long questionId;

    @JsonProperty("question__article__live")
    private boolean questionArticleLive;

    @JsonProperty("question__article__slug")
    private String questionArticleSlug;

    @JsonProperty("question__title")
    private String questionTitle;

    @JsonProperty("question__title_slug")
    private String questionTitleSlug;

    @JsonProperty("question__hide")
    private boolean questionHide;

    @JsonProperty("total_acs")
    private int totalAccepted;

    @JsonProperty("total_submitted")
    private int totalSubmitted;

    @JsonProperty("frontend_question_id")
    private int frontendQuestionId;

    @JsonProperty("is_new_question")
    private boolean isNewQuestion;


}