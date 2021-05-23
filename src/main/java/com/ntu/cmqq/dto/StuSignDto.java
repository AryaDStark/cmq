package com.ntu.cmqq.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StuSignDto {
    private Integer studentId;

    private Integer signinId;

    private Boolean isSignin;

    private String stuName;
}
