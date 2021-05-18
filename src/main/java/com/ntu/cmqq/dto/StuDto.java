package com.ntu.cmqq.dto;

import com.ntu.cmqq.entity.StuTest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Arya
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StuDto {

    private Integer id;

    private String nickname;

    private String school;

    private String description;

    private Boolean status;

    private StuTest stuTest;


}
