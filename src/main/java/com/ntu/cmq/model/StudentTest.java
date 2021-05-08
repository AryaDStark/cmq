package com.ntu.cmq.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author cmq
 * */
@Getter
@Setter
public class StudentTest {
    private Long testId;
    public Long studentId;
    private Integer score;
    private Long teachId;
    private String content;
}
