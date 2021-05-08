package com.ntu.cmq.mapper;

import com.ntu.cmq.model.Course;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cmq
 * */
@Mapper
public interface CourseMapper {
    Course getById(Long id);

}
