package sbu.hackathon.yhack.leetcode.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mayank Tiwari on 26/10/19.
 */
@Data
@EqualsAndHashCode(exclude = {""}, callSuper = true)
@Document
public class Question extends BaseEntity {

    private String description;
    private int upVoteCount;
    private int downVoteCount;
    private int acceptedCount;
    private int submissionsCount;
    private String questionFrequency;
    private Set<CompanyQuestion> companyQuestions = new HashSet<>();
    private Set<Topic> relatedTopics = new HashSet<>();

}
