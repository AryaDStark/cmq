package com.ntu.cmqq.service.impl;

import com.ntu.cmqq.entity.Student;
import com.ntu.cmqq.mapper.StudentMapper;
import com.ntu.cmqq.service.StudentService;
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
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {


}
