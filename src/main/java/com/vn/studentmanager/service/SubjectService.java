package com.vn.studentmanager.service;

import com.vn.studentmanager.entities.Subject;

import java.util.List;

public interface SubjectService {
    void create(Subject subject);
    void update(Subject subject);
    void delete(Subject subject);
    List<Subject> findAll();
    Subject findById(Long id);
    List<Subject> findByName(String key);


}
