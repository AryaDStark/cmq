package com.ntu.cmq.service.Impl;

import com.ntu.cmq.mapper.TestMapper;
import com.ntu.cmq.model.Test;
import com.ntu.cmq.service.TestService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Arya
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    TestMapper testMapper;

    @Override
    public Test getById(Long id) {
        return testMapper.getById(id);
    }

    @Override
    public List<Test> getByTeach(Long teachId) {
        return testMapper.getByTeach(teachId);
    }

    @Override
    public List<Test> getByTeachAndStu(Long teachId, Long stuId) {
        return testMapper.getByTeachAndStu(teachId,stuId);
    }

    @Override
    public int addTest(Test test) {
        return testMapper.addTest(test);
    }

    @Override
    public int updateTest(Test test) {
        return testMapper.updateTest(test);
    }

    @Override
    public int delTest(Long id) {
        return testMapper.delTest(id);
    }
}
