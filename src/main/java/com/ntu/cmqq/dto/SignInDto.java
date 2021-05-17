package com.ntu.cmqq.dto;

import com.ntu.cmqq.entity.StuSignin;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Arya
 */
@Getter
@Setter
public class SignInDto {
    private int id;

    private String pre;

    private Integer teachId;

    private Date startTime;

    private String duringTime;

    private String status;

    private List<StuSignin> stuSignins;

}
