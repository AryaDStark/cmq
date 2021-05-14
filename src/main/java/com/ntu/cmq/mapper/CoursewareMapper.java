package com.ntu.cmq.mapper;

import com.ntu.cmq.model.Courseware;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Arya
 */
@Mapper
public interface CoursewareMapper {
    int addCourseware(Courseware courseware);
    List<Courseware> getByTeach(Long teachId);
    int delByTeach(Long id);
}
