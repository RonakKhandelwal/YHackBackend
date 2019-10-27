package sbu.hackathon.yhack.leetcode.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sbu.hackathon.yhack.leetcode.model.UserStatistics;
import sbu.hackathon.yhack.leetcode.repository.QuestionRepository;
import sbu.hackathon.yhack.leetcode.repository.UserRepository;

/**
 * Created by Mayank Tiwari on 27/10/19.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/users", produces = "application/json")
public class UserController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionController questionController;

    @GetMapping("/{user_id}/stats")
    public UserStatistics getUserStatistics(@PathVariable("user_id") String userId) {
        return new UserStatistics(questionController.getAllQuestionsForUser(userId));
    }

}
