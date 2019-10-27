package sbu.hackathon.yhack.leetcode.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sbu.hackathon.yhack.leetcode.model.Question;
import sbu.hackathon.yhack.leetcode.repository.QuestionRepository;

import java.util.List;

/**
 * Created by Mayank Tiwari on 26/10/19.
 */
@Slf4j
@RestController
@RequestMapping(value = "/question", produces = "application/json")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping
    @ResponseBody
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

}
