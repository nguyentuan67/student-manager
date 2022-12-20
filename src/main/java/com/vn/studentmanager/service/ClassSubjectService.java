package com.vn.studentmanager.service;

import com.vn.studentmanager.entities.ClassSubject;

import java.util.List;

public interface ClassSubjectService {
    void create(ClassSubject classSubject);
    void update(ClassSubject classSubject);
    void delete(ClassSubject classSubject);
    List<ClassSubject> findAll();
    List<ClassSubject> findByClassId(Long id);
    List<ClassSubject> findByTeacherId(Long id);
    List<ClassSubject> findBySubjectId(Long id);
    List<Long> findClassIdByTeacherId(Long id);
    ClassSubject findByClassIdAndSubjectId(Long classId, Long subjectId);
}
