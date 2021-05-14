package com.ntu.cmq.service.Impl;

import com.ntu.cmq.mapper.CoursewareMapper;
import com.ntu.cmq.model.Courseware;
import com.ntu.cmq.service.CoursewareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Arya
 */
@Service
public class CoursewareServiceImpl implements CoursewareService {
    @Autowired
    CoursewareMapper coursewareMapper;

    @Override
    public int addCourseware(Courseware courseware) {
        return coursewareMapper.addCourseware(courseware);
    }

    @Override
    public List<Courseware> getByTeach(Long teachId) {
        return coursewareMapper.getByTeach(teachId);
    }

    @Override
    public int delByTeach(Long teachId) {
        return coursewareMapper.delByTeach(teachId);
    }
}
