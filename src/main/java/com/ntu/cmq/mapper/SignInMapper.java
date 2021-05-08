package com.ntu.cmq.mapper;

import com.ntu.cmq.model.SignIn;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cmq
 */
@Mapper
public interface SignInMapper {
    SignIn getById(Long id);
    int insertSignIN(SignIn signIn);
    int updateSignIn(SignIn signIn);
}
