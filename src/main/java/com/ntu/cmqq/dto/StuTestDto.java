package com.ntu.cmqq.dto;

import com.ntu.cmqq.entity.StuTest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Arya
 */
@Getter
@Setter
public class StuTestDto {

    private Integer id;

    private Integer teachId;

    private String time;

    private String name;

    private String content;

    private StuTest stuTest;

}
