package com.vn.studentmanager.service;

import com.vn.studentmanager.entities.User;

import java.util.List;

public interface UserService {
    void create(User user);
    void update(User user);
    void delete(User user);
    List<User> findAll();
    List<User> findByNameOrUsername(String key);
    User findById(Long id);

    List<User> findUserWithRole(Integer roleId);
}
