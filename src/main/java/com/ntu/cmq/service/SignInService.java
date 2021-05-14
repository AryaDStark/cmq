package com.ntu.cmq.service;

import com.ntu.cmq.model.SignIn;
import com.ntu.cmq.model.dto.SignInDto;

import java.util.List;

/**
 * @author cmq
 */
public interface SignInService {
    SignIn getById(Long id);
    List<SignInDto> getByTeach(Long id);
    int insertSignIn(SignIn signIn);
    int updateSignIn(SignIn signIn);
    int delSignIn(Long id);
}
