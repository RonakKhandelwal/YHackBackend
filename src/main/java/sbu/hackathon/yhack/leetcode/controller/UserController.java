package sbu.hackathon.yhack.leetcode.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sbu.hackathon.yhack.leetcode.domain.Question;
import sbu.hackathon.yhack.leetcode.domain.User;
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

    @PutMapping("/{user_id}/{question_id}")
    public User markQuestionSolved(@PathVariable("user_id") String userId, @PathVariable("question_id") String questionId, @RequestParam(value = "solved", required = false) Boolean isSolved, @RequestParam(value = "favorite", required = false) Boolean isFavorite) {
        User user = userRepository.findUserByUserName(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + userId + " Not Found!"));
        if (isSolved == null && isFavorite == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solved / Favorite Flag is Missing from the Input!");
        }
        if (isSolved != null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Is Solved Flag is Missing from the Input!");
            Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question " + questionId + " Not Found!"));
            user.getSolvedQuestions().removeIf(userQuestion -> userQuestion.getId().equals(questionId));
            if (isSolved) {
                user.getSolvedQuestions().add(question);
            }
            log.info("Marked question: {} as {} successfully!", questionId, (isSolved ? "Solved" : "Not-Solved!"));
        }
        if (isFavorite != null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Is Favorite Flag is Missing from the Input!");
            Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question " + questionId + " Not Found!"));
            user.getFavoriteQuestions().removeIf(userQuestion -> userQuestion.getId().equals(questionId));
            if (isFavorite) {
                user.getFavoriteQuestions().add(question);
            }
            log.info("Marked question: {} as {} successfully!", questionId, (isFavorite ? "Favorite" : "Un-Favorite!"));
        }
        return userRepository.save(user);
    }
}
