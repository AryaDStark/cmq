package com.ntu.cmq.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Arya
 */
@Getter
@Setter
public class Work {
    private Long id;
    private String name;
    private Long teachId;
    private String[] content=new String[1024];
}
