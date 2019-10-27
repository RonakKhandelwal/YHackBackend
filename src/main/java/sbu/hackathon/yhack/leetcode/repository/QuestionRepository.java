package sbu.hackathon.yhack.leetcode.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import sbu.hackathon.yhack.leetcode.domain.Question;

import java.util.List;
import java.util.Set;

/**
 * Created by Mayank Tiwari on 26/10/19.
 */
@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {

    List<Question> findAllByIdNotIn(Set<String> excludedIds);

    @Query(value = "{ 'companyQuestions.companyName' : { $regex : ?0, $options : 'i'}}")
    List<Question> findAllByCompanyNameLike(String companyName);

    @Query(value = "{ 'relatedTopics.name' : { $regex : ?0, $options : 'i'}}")
    List<Question> findAllByTopicNameLike(String topicName);

    List<Question> findAllByRelatedTopicsIn(List<String> topicName);

    @Query(value = "{}", fields = "{'companyQuestions.companyName': 1}")
    List<CompanyName> getDistinctCompanyName();

}
