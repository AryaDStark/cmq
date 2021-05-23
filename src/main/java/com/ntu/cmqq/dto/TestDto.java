package com.ntu.cmqq.dto;

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
public class TestDto {

    private Integer id;

    private Integer teachId;

    private String time;

    private String name;

    private String[] content;

}
