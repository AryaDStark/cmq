package com.ntu.cmq.service;

import com.ntu.cmq.model.Courseware;

import java.util.List;

/**
 * @author Arya
 */
public interface CoursewareService {
    int addCourseware(Courseware courseware);
    List<Courseware> getByTeach(Long teachId);
    int delByTeach(Long teachId);
}
