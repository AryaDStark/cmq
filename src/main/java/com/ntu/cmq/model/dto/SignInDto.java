package com.ntu.cmq.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author Arya
 */
@Getter
@Setter
public class SignInDto {
    private Long id;
    private String pre;
    private Long teachId;
    private String studentIds;
    private Date startTime;
    private String duringTime;
    private Integer status;
    private Integer inStuIds;
}
