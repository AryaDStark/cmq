package com.ntu.cmq.service.Impl;

import com.ntu.cmq.mapper.SignInMapper;
import com.ntu.cmq.model.SignIn;
import com.ntu.cmq.model.dto.SignInDto;
import com.ntu.cmq.service.SignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cmq
 */
@Service
public class SignInServiceImpl implements SignInService {
    @Autowired
    SignInMapper signInMapper;

    @Override
    public SignIn getById(Long id) {
        return signInMapper.getById(id);
    }

    @Override
    public List<SignInDto> getByTeach(Long id) {
        return signInMapper.getByTeach(id);
    }

    @Override
    public int insertSignIn(SignIn signIn) {
        return signInMapper.insertSignIN(signIn);
    }

    @Override
    public int updateSignIn(SignIn signIn) {
        return signInMapper.updateSignIn(signIn);
    }

    @Override
    public int delSignIn(Long id) {
        return signInMapper.delSignIn(id);
    }
}
