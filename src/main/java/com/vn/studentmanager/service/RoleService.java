package com.vn.studentmanager.service;

import com.vn.studentmanager.entities.Role;

import java.util.List;

public interface RoleService {
    void create(Role role);
    void update(Role role);
    void delete(Role role);
    List<Role> findAll();
    Role findById(Integer id);
}
