package sbu.hackathon.yhack.leetcode.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * Created by Mayank Tiwari on 27/10/19.
 */
@Component
public class CompanyRepositoryCustomImpl implements CompanyRepositoryCustom {

    @Autowired
    private MongoTemplate template;

    @Override
    public List<String> findAllCompanyList() {
//        mongoTemplate.aggregate(Aggregation.newAggregation(
//                Aggregation.project("companyQuestions.companyName"),
//                Aggregation.unwind("companyQuestions.companyName"),
//                Aggregation.group("companyQuestions.companyName")
//        ).withOptions(
//
//        ));
//
//        Aggregation aggregation = Aggregation.newAggregation(
//                Aggregation.unwind("companyQuestions.companyName"),
//                Aggregation.group("companyQuestions.companyName")
//        ).withOptions(new AggregationOptions.Builder().build());
        AggregationOptions options = AggregationOptions.builder().build();

        Aggregation aggregation = newAggregation(
                project("companyQuestions"),
                unwind("companyQuestions")
//                group("tags").count().as("count")
        ).withOptions(options);

        AggregationResults<String> results = template.aggregate(aggregation, "question", String.class);
        return results.getMappedResults();
    }
}
