package com.ntu.cmq.model.dto;

import com.ntu.cmq.model.Test;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Arya
 */
@Getter
@Setter
public class StuTestDto {
    private Long id;
    private Long testId;
    private Long stuId;
    private Integer score;
    private Long teachId;
    private String[] content=new String[1024];
}
