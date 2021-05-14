package com.ntu.cmq.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Arya
 */
@Getter
@Setter
public class Courseware {
    private Long id;
    private Long teachId;
    private String content;
}
