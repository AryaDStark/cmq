package com.ntu.cmqq.util;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * @author cmq
 */
@Getter
@Setter
public class Result {
    private Boolean success;
    private Integer code;
    private String msg;
    private HashMap<String,Object> data = new HashMap<>() ;

    public static Result ok(){
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(200);
        return result;
    }
    public static Result fail(){
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(400);
        return result;
    }

    private void setSuccess(Boolean success) {
        this.success = success;
    }

   private void setCode(Integer code) {
        this.code = code;
    }

    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Result setData(String a,Object b) {
        data.put(a,b);
        return this;
    }
}
