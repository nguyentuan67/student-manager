package com.vn.studentmanager.service;

import com.vn.studentmanager.entities.Result;
import com.vn.studentmanager.model.MarkModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface ResultService {
    void create(Result result);
    List<Result> saveAll(List<Result> results);
    void update(Result result);
    void delete(Result result);
    List<Result> findAll();
    List<Object[]> getStudentMarkByClassIdAndSubjectId(Long subjectId, Long classId);
    Result findResultByUserIdAndSubjectId(Long subjectId, Long userId);

    List<Result> findUserResults(Long userId);
}
