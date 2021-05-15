package com.ntu.cmq.mapper;

import com.ntu.cmq.model.Mean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Arya
 */
@Mapper
public interface MeanMapper {
    Mean getById(Long id);
    List<Mean> getByTeach(Long teachId);
    int addMean(Mean mean);


}
