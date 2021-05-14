package com.ntu.cmq.service.Impl;

import com.ntu.cmq.mapper.StuWorkMapper;
import com.ntu.cmq.model.StuWork;
import com.ntu.cmq.model.Work;
import com.ntu.cmq.service.StuWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Arya
 */
@Service
public class StuWorkServiceImpl implements StuWorkService {
    @Autowired
    StuWorkMapper stuWorkMapper;

    @Override
    public int addStuWork(StuWork stuWork) {
        return stuWorkMapper.addStuWork(stuWork);
    }

    @Override
    public Work getScore(Long workId, Long stuId) {
        return stuWorkMapper.getScore(workId,stuId);
    }
}
