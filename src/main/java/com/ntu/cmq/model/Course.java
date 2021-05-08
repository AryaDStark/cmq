package com.ntu.cmq.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author cmq
 */
@Getter
@Setter
public class Course {
    private Long id;
    private String name;
    private String imgUrl;
    private String content;
    private int classNum;
}
