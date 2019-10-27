package sbu.hackathon.yhack.leetcode.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sbu.hackathon.yhack.leetcode.domain.CompanyQuestion;
import sbu.hackathon.yhack.leetcode.domain.Question;
import sbu.hackathon.yhack.leetcode.repository.CompanyRepository;
import sbu.hackathon.yhack.leetcode.repository.QuestionRepository;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mayank Tiwari on 27/10/19.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/companies", produces = "application/json")
public class CompanyController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping
    public Set<String> getAllCompanyNames() {
//        return companyRepository.findAll().stream().map(CompanyQuestion::getCompanyName).collect(Collectors.toList());
//        return questionRepository.getDistinctCompanyName().stream().map(CompanyName::getCompanyName).collect(Collectors.toList());
        Set<String> companies = new HashSet<>();
        for (Question question : questionRepository.findAll()) {
            for (CompanyQuestion companyQuestion : question.getCompanyQuestions()) {
                companies.add(companyQuestion.getCompanyName());
            }
        }
        return companies;
    }

}
