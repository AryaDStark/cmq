package com.ntu.cmq.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author cmq
 * */
@Getter
@Setter
public class User {
    private Long id;
    private String username;
    private String password;
    private String name;
    private Integer status;
    private String topTeachIds;
    private String teachIds;
}
