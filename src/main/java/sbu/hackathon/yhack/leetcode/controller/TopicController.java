package sbu.hackathon.yhack.leetcode.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sbu.hackathon.yhack.leetcode.domain.Question;
import sbu.hackathon.yhack.leetcode.domain.Topic;
import sbu.hackathon.yhack.leetcode.repository.QuestionRepository;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mayank Tiwari on 27/10/19.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/topics", produces = "application/json")
public class TopicController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping
    public Set<String> getAllCompanyNames() {
        Set<String> topics = new HashSet<>();
        for (Question question : questionRepository.findAll()) {
            for (Topic relatedTopic : question.getRelatedTopics()) {
                topics.add(relatedTopic.getName());
            }
        }
        return topics;
    }

}
