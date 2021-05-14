package com.ntu.cmq.service;

import com.ntu.cmq.model.Test;

import java.util.List;

/**
 * @author Arya
 */
public interface TestService {
    Test getById(Long id);
    List<Test> getByTeach(Long teachId);
    List<Test> getByTeachAndStu(Long teachId,Long stuId);
    int addTest(Test test);
    int updateTest(Test test);
    int delTest(Long id);
}
