package com.ntu.cmq.mapper;

import com.ntu.cmq.model.dto.WorkDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Arya
 */
@Mapper
public interface WorkMapper {
    List<WorkDto> getByTeach(Long teachId);
    int addWork(WorkDto workDto);
}
