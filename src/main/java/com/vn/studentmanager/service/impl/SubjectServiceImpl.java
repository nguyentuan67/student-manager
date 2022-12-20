package com.vn.studentmanager.service.impl;

import com.vn.studentmanager.entities.Subject;
import com.vn.studentmanager.repositories.SubjectRepo;
import com.vn.studentmanager.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    SubjectRepo subjectRepo;

    @Override
    public void create(Subject subject) {
        subjectRepo.save(subject);
    }

    @Override
    public void update(Subject subject) {
        subjectRepo.save(subject);
    }

    @Override
    public void delete(Subject subject) {
        subjectRepo.delete(subject);
    }

    @Override
    public List<Subject> findAll() {
        return subjectRepo.findAll();
    }

    @Override
    public Subject findById(Long id) {
        return subjectRepo.findTopById(id);
    }

    @Override
    public List<Subject> findByName(String key) {
        return subjectRepo.findByName(key);
    }
}
