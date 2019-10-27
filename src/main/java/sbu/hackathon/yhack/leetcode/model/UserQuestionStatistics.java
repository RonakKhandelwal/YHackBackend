package sbu.hackathon.yhack.leetcode.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sbu.hackathon.yhack.leetcode.domain.Question;
import sbu.hackathon.yhack.leetcode.domain.Topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mayank Tiwari on 27/10/19.
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserQuestionStatistics {

    @JsonProperty("overall_stats")
    private UserStatistics userStatistics;

    @JsonProperty("topic_stats")
    private List<TopicStatistics> topicStatistics;

    @JsonProperty("questions")
    private List<QuestionUserModel> questionUserModelList;

    public UserQuestionStatistics(List<QuestionUserModel> questionUserModelList) {
        this.questionUserModelList = questionUserModelList;

        /*Map<Integer, Long> difficultyStatus = questionUserModelList.stream()
                .filter(QuestionUserModel::isSolved)
                .collect(Collectors.groupingBy(questionUserModel -> questionUserModel.getQuestion().getDifficultyLevel(), Collectors.counting()));*/
        Map<Integer, Long> difficultyStatus = new HashMap<>();
        Map<String, Long> topicQuestionsSolved = new HashMap<>();
        Map<String, Long> topicTotalQuestions = new HashMap<>();

        for (QuestionUserModel questionUserModel : questionUserModelList) {
            Question question = questionUserModel.getQuestion();
            boolean solved = questionUserModel.isSolved();
            for (Topic relatedTopic : question.getRelatedTopics()) {
                String topicName = relatedTopic.getName();
                topicTotalQuestions.put(topicName, topicTotalQuestions.getOrDefault(topicName, 0L) + 1);
                if (solved) {
                    topicQuestionsSolved.put(topicName, topicQuestionsSolved.getOrDefault(topicName, 0L) + 1);
                }
            }
            if (solved) {
                int difficultyLevel = question.getDifficultyLevel();
                Long questionsSolvedByDifficulty = difficultyStatus.getOrDefault(difficultyLevel, 0L) + 1;
                difficultyStatus.put(difficultyLevel, questionsSolvedByDifficulty);
            }
        }

        this.userStatistics = new UserStatistics(
                difficultyStatus.getOrDefault(1, 0L),
                difficultyStatus.getOrDefault(2, 0L),
                difficultyStatus.getOrDefault(3, 0L)
        );

        this.topicStatistics = new ArrayList<>();
        for (String topicName : topicTotalQuestions.keySet()) {
            this.topicStatistics.add(new TopicStatistics(
                    topicName,
                    topicQuestionsSolved.getOrDefault(topicName, 0L),
                    topicTotalQuestions.get(topicName)
            ));
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class TopicStatistics {

        private String topicName;
        private Long questionsSolved;
        private Long totalQuestions;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class UserStatistics {

        private long noEasySolved;
        private long noMediumSolved;
        private long noHardSolved;

    }

}
