package sbu.hackathon.yhack.leetcode.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mayank Tiwari on 26/10/19.
 */
@Slf4j
@Data
@EqualsAndHashCode(exclude = {""}, callSuper = true)
@NoArgsConstructor
@Document
public class User extends BaseEntity {

    private String userName;
    private String password;
    private Name name;
    private String email;
    private Boolean isActive = true;
    private LocalDate startDate;
    private LocalDate endDate;

    @DBRef
    private Set<Question> solvedQuestions = new HashSet<>();

    @DBRef
    private Set<Question> favoriteQuestions = new HashSet<>();

    @Autowired
    @Transient
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String getDisplayName() {
        if (name == null) {
            return "";
        } else {
            return name.getDisplayName();
        }
    }

    @JsonProperty
    public void setPassword(String password) {
        log.trace("Encoding password: {}", password);
        if (!StringUtils.isEmpty(password)) {
            this.password = passwordEncoder.encode(password);
        }
    }
}
