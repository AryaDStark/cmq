package com.ntu.cmq.service;

import com.ntu.cmq.model.StuTest;

/**
 * @author Arya
 */
public interface StuTestService {
    int addStuTest(StuTest stuTest);
    int updateScore(Long id,Integer score);
}
