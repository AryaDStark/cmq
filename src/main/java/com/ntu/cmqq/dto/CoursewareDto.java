package com.ntu.cmqq.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Arya
 */
@Getter
@Setter
public class CoursewareDto {
    private Integer id;

    private Integer teachId;

    private String name;

    private String[] content;
}
