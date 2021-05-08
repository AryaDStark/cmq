package com.ntu.cmq.service.Impl;

import com.ntu.cmq.mapper.CourseMapper;
import com.ntu.cmq.model.Course;
import com.ntu.cmq.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cmq
 */
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseMapper courseMapper;

    @Override
    public Course getById(Long id) {
        return courseMapper.getById(id);
    }
}
