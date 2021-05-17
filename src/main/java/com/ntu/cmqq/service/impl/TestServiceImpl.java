package com.ntu.cmqq.service.impl;

import com.ntu.cmqq.entity.Test;
import com.ntu.cmqq.mapper.TestMapper;
import com.ntu.cmqq.service.TestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cmq
 * @since 2021-05-17
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {

}
