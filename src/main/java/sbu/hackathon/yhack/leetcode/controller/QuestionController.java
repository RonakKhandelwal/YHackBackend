package sbu.hackathon.yhack.leetcode.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sbu.hackathon.yhack.leetcode.domain.Question;
import sbu.hackathon.yhack.leetcode.domain.User;
import sbu.hackathon.yhack.leetcode.model.QuestionUserModel;
import sbu.hackathon.yhack.leetcode.model.UserQuestionStatistics;
import sbu.hackathon.yhack.leetcode.repository.QuestionRepository;
import sbu.hackathon.yhack.leetcode.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Mayank Tiwari on 26/10/19.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/questions", produces = "application/json")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @ResponseBody
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @GetMapping("/user/{user_id}")
    public UserQuestionStatistics getAllQuestionsForUser(@PathVariable("user_id") String userId) {
        Optional<User> userByUserName = userRepository.findUserByUserName(userId);
        Set<String> solvedQuestionId = new HashSet<>();
        if (userByUserName.isPresent()) {
            solvedQuestionId = userByUserName.get().getSolvedQuestions().stream().map(Question::getId).collect(Collectors.toSet());
        }

        Set<String> finalSolvedQuestionId = solvedQuestionId;
        List<QuestionUserModel> questionUserModelList = getAllQuestions().stream()
                .map(question -> {
                    QuestionUserModel questionUserModel = new QuestionUserModel(question);
                    if (!finalSolvedQuestionId.isEmpty() && finalSolvedQuestionId.contains(question.getId())) {
                        questionUserModel.setSolved(true);
                    }
                    return questionUserModel;
                })
                .collect(Collectors.toList());

        return new UserQuestionStatistics(questionUserModelList);
    }

}
