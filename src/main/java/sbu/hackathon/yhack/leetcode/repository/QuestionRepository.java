package sbu.hackathon.yhack.leetcode.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sbu.hackathon.yhack.leetcode.model.Question;

/**
 * Created by Mayank Tiwari on 26/10/19.
 */
@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {
}
