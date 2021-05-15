package com.ntu.cmq.mapper;

import com.ntu.cmq.model.Courseware;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Arya
 */
@Mapper
public interface CoursewareMapper {
    List<Courseware> getByTeach(Long teachId);
    int addCourseware(Courseware courseware);
    int delByTeach(Long id);
}
