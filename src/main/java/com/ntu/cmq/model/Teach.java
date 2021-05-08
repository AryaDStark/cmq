package com.ntu.cmq.model;


import lombok.Getter;
import lombok.Setter;

/**
 * @author cmq
 */
@Getter
@Setter
public class Teach {
    private Long id;
    private Long teacherId;
    private Long courseId;
    private String studentIds;

}
