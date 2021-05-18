package com.ntu.cmqq.dto;

import com.ntu.cmqq.entity.Course;
import com.ntu.cmqq.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Arya
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeachDto {
    private int id;
    private int teacherId;
    private Course course;
    private boolean isTop;
    private String description;
    private Teacher teacher;
}
