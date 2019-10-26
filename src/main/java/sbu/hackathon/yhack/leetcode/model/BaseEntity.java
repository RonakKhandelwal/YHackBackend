package sbu.hackathon.yhack.leetcode.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * Created by Mayank Tiwari on 26/10/19.
 */
@Data
public class BaseEntity implements Serializable {

    @Id
    private String id;

}
