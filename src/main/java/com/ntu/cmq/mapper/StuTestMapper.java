package com.ntu.cmq.mapper;

import com.ntu.cmq.model.StuTest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Arya
 */
@Mapper
public interface StuTestMapper {
    int addStuTest(StuTest stuTest);
    int updateScore(@Param("id")Long id,@Param("score")Integer score);
}
