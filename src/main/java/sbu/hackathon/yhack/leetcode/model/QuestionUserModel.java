package sbu.hackathon.yhack.leetcode.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.NoArgsConstructor;
import sbu.hackathon.yhack.leetcode.domain.Question;

/**
 * Created by Mayank Tiwari on 27/10/19.
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QuestionUserModel {

    @JsonUnwrapped
    private Question question;

    private boolean isSolved;

    public QuestionUserModel(Question question) {
        this.question = question;
    }
}
