package com.ntu.cmq.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author cmq
 * */
@Getter
@Setter
public class SignIn {
    private Long id;
    private String pre;
    private Long teachId;
    private String studentIds;
    private Date startTime;
    private String duringTime;
    private Integer status;
}
