package com.vn.studentmanager.service.impl;

import com.vn.studentmanager.entities.ClassSubject;
import com.vn.studentmanager.repositories.ClassSubjectRepo;
import com.vn.studentmanager.service.ClassSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassSubjectServiceImpl implements ClassSubjectService {
    @Autowired
    ClassSubjectRepo classSubjectRepo;

    @Override
    public void create(ClassSubject classSubject) {
        classSubjectRepo.save(classSubject);
    }

    @Override
    public void update(ClassSubject classSubject) {
        classSubjectRepo.save(classSubject);
    }

    @Override
    public void delete(ClassSubject classSubject) {
        classSubjectRepo.delete(classSubject);
    }

    @Override
    public List<ClassSubject> findAll() {
        return classSubjectRepo.findAll();
    }

    @Override
    public List<ClassSubject> findByClassId(Long id) {
        return classSubjectRepo.findByClassId(id);
    }
    @Override
    public List<ClassSubject> findByTeacherId(Long id) {
        return classSubjectRepo.findByTeacherId(id);
    }
    @Override
    public List<ClassSubject> findBySubjectId(Long id) {
        return classSubjectRepo.findBySubjectId(id);
    }

    @Override
    public List<Long> findClassIdByTeacherId(Long id) {
        return classSubjectRepo.findClassIdByTeacherId(id);
    }

    @Override
    public ClassSubject findByClassIdAndSubjectId(Long classId, Long subjectId) {
        return classSubjectRepo.findByClassIdAndSubjectId(classId, subjectId);
    }
}
