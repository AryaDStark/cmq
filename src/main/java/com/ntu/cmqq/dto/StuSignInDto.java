package com.ntu.cmqq.dto;

import com.ntu.cmqq.entity.StuSignin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StuSignInDto {

    private int id;

    private String pre;

    private Integer teachId;

    private Date startTime;

    private String duringTime;

    private String status;

    private StuSignin stuSignin;

}
