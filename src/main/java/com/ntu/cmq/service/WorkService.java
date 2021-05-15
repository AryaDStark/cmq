package com.ntu.cmq.service;

import com.ntu.cmq.model.dto.WorkDto;

import java.util.List;

/**
 * @author Arya
 */
public interface WorkService {
    List<WorkDto> getByTeach(Long id);
    int addWork(WorkDto workDto);
}
