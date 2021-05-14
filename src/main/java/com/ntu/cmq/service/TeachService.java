package com.ntu.cmq.service;

import com.ntu.cmq.model.Teach;

import java.util.List;

/**
 * @author cmq
 */
public interface TeachService {
    List<Teach> getAllTeach();
    Teach getById(Long id);
    int insertTeach(Teach teach);
    int updateTeach(Teach teach);
    int delTeach(Long id);
}
