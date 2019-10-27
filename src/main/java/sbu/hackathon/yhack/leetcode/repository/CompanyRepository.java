package sbu.hackathon.yhack.leetcode.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sbu.hackathon.yhack.leetcode.domain.CompanyQuestion;

/**
 * Created by Mayank Tiwari on 27/10/19.
 */
@Repository
public interface CompanyRepository extends MongoRepository<CompanyQuestion, String>, CompanyRepositoryCustom {

}
