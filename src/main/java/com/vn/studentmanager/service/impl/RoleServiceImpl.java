package com.vn.studentmanager.service.impl;

import com.vn.studentmanager.entities.Role;
import com.vn.studentmanager.repositories.RoleRepo;
import com.vn.studentmanager.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepo roleRepo;

    @Override
    public void create(Role role) {
        roleRepo.save(role);
    }

    @Override
    public void update(Role role) {
        roleRepo.save(role);
    }

    @Override
    public void delete(Role role) {
        roleRepo.delete(role);
    }

    @Override
    public List<Role> findAll() {
        return roleRepo.findAll();
    }

    @Override
    public Role findById(Integer id) {
        return roleRepo.findTopById(id);
    }
}
