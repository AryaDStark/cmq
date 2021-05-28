package com.ntu.cmqq.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Arya
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto implements Comparable<StudentDto>{
    private Integer id;

    private String nickname;

    private String school;

    private String description;

    private Boolean status;

    private String username;

    private String password;

    private Double score;

    @Override
    public int compareTo(StudentDto o) {
        return Integer.parseInt(this.score-o.score+"");
    }
}
