package sbu.hackathon.yhack.leetcode.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Mayank Tiwari on 26/10/19.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(exclude = {""}, callSuper = true)
@Document
//public class Topic extends BaseEntity {
public class Topic {

    private String name;

}
