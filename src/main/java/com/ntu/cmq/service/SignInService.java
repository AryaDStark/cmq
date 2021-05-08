package com.ntu.cmq.service;

import com.ntu.cmq.model.SignIn;

/**
 * @author cmq
 */
public interface SignInService {
    SignIn getById(Long id);
    int insertSignIn(SignIn signIn);
    int updateSignIn(SignIn signIn);
}
