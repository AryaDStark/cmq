package com.ntu.cmq.service;

import com.ntu.cmq.model.Mean;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Arya
 */
public interface MeanService {
    Mean getById(Long id);
    List<Mean> getByTeach(Long teachId);
    int addMean(Mean mean);

}
