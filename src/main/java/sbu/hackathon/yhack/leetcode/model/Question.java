package sbu.hackathon.yhack.leetcode.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import sbu.hackathon.yhack.leetcode.rest.QuestionObject;
import sbu.hackathon.yhack.leetcode.rest.StatStatusPairObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mayank Tiwari on 26/10/19.
 */
@Data
@EqualsAndHashCode(exclude = {""}, callSuper = true)
@NoArgsConstructor
@Document
public class Question extends BaseEntity {

    private String title;
    private String description;
    private int difficultyLevel;
    private int upVoteCount;
    private int downVoteCount;
    private int acceptedCount;
    private int submissionsCount;
    private String questionFrequency;
    private Set<CompanyQuestion> companyQuestions = new HashSet<>();
    private Set<Topic> relatedTopics = new HashSet<>();
    private Metadata metadata = new Metadata();

    @Data
    @Document
    private static class Metadata {

        private Long leetcodeId;
        private Long leetcodeFrontendId;
        private String titleSlug;
        private boolean isNewQuestion;
        private boolean isHiddenQuestion;
    }

    public Question(StatStatusPairObject statStatusPairObject) {
        QuestionObject questionObject = statStatusPairObject.getQuestionObject();

        title = questionObject.getQuestionTitle();
        difficultyLevel = statStatusPairObject.getDifficulty().getLevel();
        acceptedCount = questionObject.getTotalAccepted();
        submissionsCount = questionObject.getTotalSubmitted();

        metadata.leetcodeId = questionObject.getQuestionId();
        metadata.leetcodeFrontendId = questionObject.getFrontendQuestionId();
        metadata.titleSlug = questionObject.getQuestionTitleSlug();
        metadata.isNewQuestion = questionObject.isNewQuestion();
        metadata.isHiddenQuestion = questionObject.isQuestionHide();
    }

    public String getURL() {
        return "https://leetcode.com/problems/" + metadata.titleSlug + "/";
    }
}
