package com.ntu.cmq.mapper;

import com.ntu.cmq.model.SignIn;
import com.ntu.cmq.model.dto.SignInDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author cmq
 */
@Mapper
public interface SignInMapper {
    SignIn getById(Long id);
    List<SignInDto> getByTeach(Long id);
    int insertSignIN(SignIn signIn);
    int updateSignIn(SignIn signIn);
    int delSignIn(Long id);
}
