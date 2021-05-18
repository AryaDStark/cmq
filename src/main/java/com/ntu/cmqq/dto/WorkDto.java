package com.ntu.cmqq.dto;

import com.ntu.cmqq.entity.StuWork;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Arya
 */
@Getter
@Setter
public class WorkDto {
    private Integer id;

    private Integer teachId;

    private String[] content;

    private String name;

    private StuWork stuWork;

}
