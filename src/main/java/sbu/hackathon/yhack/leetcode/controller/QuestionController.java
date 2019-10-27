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

import java.util.*;
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
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @GetMapping("/user/{user_id}/next")
    public Question getNextQuestion(@PathVariable("user_id") String userId) {
        Optional<User> userByUserName = userRepository.findUserByUserName(userId);
        Set<String> solvedQuestionId = new HashSet<>();
        if (userByUserName.isPresent()) {
            solvedQuestionId = userByUserName.get().getSolvedQuestions().stream().map(Question::getId).collect(Collectors.toSet());
            Set<String> favoriteQuestions = userByUserName.get().getFavoriteQuestions().stream().map(Question::getId).collect(Collectors.toSet());
            solvedQuestionId.addAll(favoriteQuestions);
        }
        List<Question> questionList = questionRepository.findAllByIdNotIn(solvedQuestionId);
        Collections.shuffle(questionList);
        return questionList.get(0);
    }

    @GetMapping("/user/{user_id}")
    public UserQuestionStatistics getAllQuestionsForUser(@PathVariable("user_id") String userId) {
        Optional<User> userByUserName = userRepository.findUserByUserName(userId);
        Set<String> excludedQuestionId = new HashSet<>();
        Set<String> favoriteQuestionId = new HashSet<>();
        if (userByUserName.isPresent()) {
            excludedQuestionId = userByUserName.get().getSolvedQuestions().stream().map(Question::getId).collect(Collectors.toSet());
            favoriteQuestionId = userByUserName.get().getFavoriteQuestions().stream().map(Question::getId).collect(Collectors.toSet());
        }

        Set<String> finalSolvedQuestionId = excludedQuestionId;
        Set<String> finalFavoriteQuestionId = favoriteQuestionId;
        List<QuestionUserModel> questionUserModelList = getAllQuestions().stream()
                .map(question -> {
                    QuestionUserModel questionUserModel = new QuestionUserModel(question);
                    if (!finalSolvedQuestionId.isEmpty() && finalSolvedQuestionId.contains(question.getId())) {
                        questionUserModel.setSolved(true);
                    }
                    if (!finalFavoriteQuestionId.isEmpty() && finalFavoriteQuestionId.contains(question.getId())) {
                        questionUserModel.setFavorite(true);
                    }
                    return questionUserModel;
                })
                .collect(Collectors.toList());

        return new UserQuestionStatistics(questionUserModelList);
    }

    @GetMapping("/company/{company_name}")
    public List<Question> getAllQuestionsForCompany(@PathVariable String company_name) {
        return questionRepository.findAllByCompanyNameLike(company_name);
//        return questionRepository.findAllByCompanyQuestions_CompanyNameLike(company_name);
    }

    @GetMapping("/topic/{topic_name}")
    public List<Question> getAllQuestionsForTopic(@PathVariable String topic_name) {
//        questionRepository.findAllByRelatedTopicsIn(Collections.singletonList(topic_name));
        return questionRepository.findAllByTopicNameLike(topic_name);
    }

}
