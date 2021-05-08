package com.ntu.cmq.service;

import com.ntu.cmq.model.User;

/**
 * @author cmq
 */
public interface UserService {
    User getByUsername(String username);

    User getById(Long id);

    int insertUser(User user);

    int updateUser(User user);
}
