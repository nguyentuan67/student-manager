package com.vn.studentmanager.service.impl;

import com.vn.studentmanager.entities.Classroom;
import com.vn.studentmanager.entities.Subject;
import com.vn.studentmanager.repositories.ClassroomRepo;
import com.vn.studentmanager.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomServiceImpl implements ClassroomService {
    @Autowired
    ClassroomRepo classroomRepo;

    @Override
    public void create(Classroom classroom) {
        classroomRepo.save(classroom);
    }

    @Override
    public void update(Classroom classroom) {
        classroomRepo.save(classroom);
    }

    @Override
    public void delete(Classroom classroom) {
        classroomRepo.delete(classroom);
    }

    @Override
    public Classroom findById(Long id) {
        return classroomRepo.findTopById(id);
    }

    @Override
    public List<Classroom> findAll() {
        return classroomRepo.findAll();
    }

    @Override
    public List<Classroom> findByName(String key) {
        return classroomRepo.findByName(key);
    }

    @Override
    public List<Subject> subjectLevelClass(int gradeClass) {
        return classroomRepo.subjectLevelClass(gradeClass);
    }

    @Override
    public List<Classroom> findClassByTeacherId(Long teacherId) {
        return classroomRepo.findClassByTeacherId(teacherId);
    }
}
