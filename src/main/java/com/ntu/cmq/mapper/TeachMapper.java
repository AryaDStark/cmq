package com.ntu.cmq.mapper;

import com.ntu.cmq.model.Teach;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author cmq
 */
@Mapper
public interface TeachMapper {
    List<Teach> getAllTeach();
    Teach getById(Long id);
    int insertTeach(Teach teach);
    int updateTeach(Teach teach);
}
