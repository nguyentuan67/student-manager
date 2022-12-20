package com.vn.studentmanager.service;

import com.vn.studentmanager.entities.Classroom;
import com.vn.studentmanager.entities.Subject;

import java.util.List;

public interface ClassroomService {
    void create(Classroom classroom);
    void update(Classroom classroom);
    void delete(Classroom classroom);
    Classroom findById(Long id);
    List<Classroom> findAll();
    List<Classroom> findByName(String key);
    List<Subject> subjectLevelClass(int gradeClass);
    List<Classroom> findClassByTeacherId(Long teacherId);
}
