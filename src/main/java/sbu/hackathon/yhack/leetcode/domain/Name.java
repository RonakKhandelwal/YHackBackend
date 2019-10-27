package sbu.hackathon.yhack.leetcode.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sbu.hackathon.yhack.leetcode.util.Commons;

/**
 * Created by Mayank Tiwari on 2019-01-29.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Name {

    private String firstName;
    private String middleName;
    private String lastName;

    public String getFullName() {
        return Commons.listToStringWithSpaces(firstName, middleName, lastName);
    }

    public String getDisplayName() {
        return Commons.listToStringWithSpaces(firstName, lastName);
    }
}
