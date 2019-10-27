package sbu.hackathon.yhack.leetcode.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Mayank Tiwari on 27/10/19.
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserQuestionStatistics {

    @JsonProperty("stats")
    private UserStatistics userStatistics;

    @JsonProperty("questions")
    private List<QuestionUserModel> questionUserModelList;

    public UserQuestionStatistics(List<QuestionUserModel> questionUserModelList) {
        this.questionUserModelList = questionUserModelList;

        Map<Integer, Long> difficultyStatus = questionUserModelList.stream()
                .filter(QuestionUserModel::isSolved)
                .collect(Collectors.groupingBy(questionUserModel -> questionUserModel.getQuestion().getDifficultyLevel(), Collectors.counting()));
        this.userStatistics = new UserStatistics(
                difficultyStatus.getOrDefault(1, 0L),
                difficultyStatus.getOrDefault(2, 0L),
                difficultyStatus.getOrDefault(3, 0L)
        );
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class UserStatistics {

        private long noEasySolved;
        private long noMediumSolved;
        private long noHardSolved;

    }

}
