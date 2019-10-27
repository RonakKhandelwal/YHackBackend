package sbu.hackathon.yhack.leetcode.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sbu.hackathon.yhack.leetcode.domain.User;

import java.util.Optional;

/**
 * Created by Mayank Tiwari on 26/10/19.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findUserByUserName(String userName);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    Optional<User> findByDisplayName(String name);

    Optional<User> findByEmail(String email);

}
