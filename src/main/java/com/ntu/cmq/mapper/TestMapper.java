package com.ntu.cmq.mapper;

import com.ntu.cmq.model.Test;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Arya
 */
@Mapper
public interface TestMapper {
    Test getById(Long id);
    List<Test> getByTeach(Long teachId);
    List<Test> getByTeachAndStu(@Param("t") Long teachId,@Param("s") Long stuId);
    int addTest(Test test);
    int updateTest(Test test);
    int delTest(Long id);
}
