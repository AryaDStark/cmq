package com.ntu.cmq.mapper;

import com.ntu.cmq.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cmq
 */
@Mapper
public interface UserMapper {

    User getByUsername(String username);
    User getById(Long id);
    int insertUser(User user);
    int updateUser(User user);


}
