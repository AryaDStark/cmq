package com.ntu.cmq.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Arya
 */
@Getter
@Setter
public class TestDto {
    private Long id;
    private Long teachId;
    private String[] content = new String[1024];
    private String time;
}
