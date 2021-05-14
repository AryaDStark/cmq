package com.ntu.cmq.service.Impl;

import com.ntu.cmq.mapper.WorkMapper;
import com.ntu.cmq.model.Work;
import com.ntu.cmq.model.dto.WorkDto;
import com.ntu.cmq.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Arya
 */
@Service
public class WorkServiceImpl implements WorkService {
    @Autowired
    WorkMapper workMapper;

    @Override
    public List<WorkDto> getByTeach(Long id) {
        return workMapper.getByTeach(id);
    }

    @Override
    public int addWork(WorkDto workDto) {
        return workMapper.addWork(workDto);
    }

}
