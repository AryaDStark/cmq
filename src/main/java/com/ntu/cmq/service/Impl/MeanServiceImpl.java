package com.ntu.cmq.service.Impl;

import com.ntu.cmq.mapper.MeanMapper;
import com.ntu.cmq.model.Mean;
import com.ntu.cmq.service.MeanService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Arya
 */
@Service
public class MeanServiceImpl implements MeanService {
    @Autowired
    MeanMapper meanMapper;

    @Override
    public Mean getById(Long id) {
        return meanMapper.getById(id);
    }

    @Override
    public List<Mean> getByTeach(Long teachId) {
        return meanMapper.getByTeach(teachId);
    }

    @Override
    public int addMean(Mean mean) {
        return meanMapper.addMean(mean);
    }
}
