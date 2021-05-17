package com.ntu.cmqq.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Arya
 */
@Setter
@Getter
public class TeachTop {
    private int id;
    private int teachId;
    private int status;
    private Boolean isTop;
}
