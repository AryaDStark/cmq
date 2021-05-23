package com.ntu.cmqq.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TestStuDto {
    private Integer testId;

    private Integer studentId;

    private Integer score;

    private String[] content;

    private Boolean isPush;
}
