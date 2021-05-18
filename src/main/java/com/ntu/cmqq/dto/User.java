package com.ntu.cmqq.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Arya
 */
@Getter
@Setter
public class User {
//    username  nickname  password  status  school
    private int id;
    private String username;
    private String password;
    private String nickname;
    private String school;
    private int status;     //1学生  0老师
    private String pPassword;

}
