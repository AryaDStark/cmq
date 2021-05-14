package com.ntu.cmq.service.Impl;

import com.ntu.cmq.mapper.TeachMapper;
import com.ntu.cmq.model.Teach;
import com.ntu.cmq.service.TeachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cmq
 */
@Service
public class TeachServiceImpl implements TeachService {

    @Autowired
    TeachMapper teachMapper;

    @Override
    public List<Teach> getAllTeach() {
        return teachMapper.getAllTeach();
    }

    public Teach getById(Long id){ return teachMapper.getById(id); }

    @Override
    public int insertTeach(Teach teach) {
        return teachMapper.insertTeach(teach);
    }

    @Override
    public int updateTeach(Teach teach) {
        return teachMapper.updateTeach(teach);
    }

    @Override
    public int delTeach(Long id) {
        return teachMapper.delTeach(id);
    }
}
