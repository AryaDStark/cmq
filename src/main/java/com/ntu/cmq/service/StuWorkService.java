package com.ntu.cmq.service;

import com.ntu.cmq.model.StuWork;
import com.ntu.cmq.model.Work;

/**
 * @author Arya
 */
public interface StuWorkService {
    int addStuWork(StuWork stuWork);
    Work getScore(Long workId,Long stuId);
}
