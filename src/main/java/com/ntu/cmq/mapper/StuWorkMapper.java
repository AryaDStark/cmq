package com.ntu.cmq.mapper;

import com.ntu.cmq.model.StuWork;
import com.ntu.cmq.model.Work;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Arya
 */
@Mapper
public interface StuWorkMapper {

    int addStuWork(StuWork stuWork);
    Work getScore(@Param("workId")Long workId,@Param("stuId")Long stuId);
}
