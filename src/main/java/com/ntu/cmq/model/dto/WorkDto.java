package com.ntu.cmq.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Arya
 */
@Getter
@Setter
public class WorkDto {
    private Long id;
    private String name;
    private Long teachId;
    private String content;
}
