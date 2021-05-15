package com.ntu.cmq.service.Impl;

import com.ntu.cmq.mapper.StuTestMapper;
import com.ntu.cmq.model.StuTest;
import com.ntu.cmq.service.StuTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Arya
 */
@Service
public class StuTestServiceImpl implements StuTestService {

    @Autowired
    StuTestMapper stuTestMapper;

    @Override
    public int addStuTest(StuTest stuTest) {
        return stuTestMapper.addStuTest(stuTest);
    }

    @Override
    public int updateScore(Long id, Integer score) {
        return stuTestMapper.updateScore(id,score);
    }
}
