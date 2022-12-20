package com.vn.studentmanager.service.impl;

import com.vn.studentmanager.entities.Result;
import com.vn.studentmanager.model.MarkModel;
import com.vn.studentmanager.repositories.ResultRepo;
import com.vn.studentmanager.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResultServiceImpl implements ResultService {
    @Autowired
    ResultRepo resultRepo;
    @Override
    public void create(Result result) {
        resultRepo.save(result);
    }

    @Override
    public List<Result> saveAll(List<Result> results) {
        List<Result> savedResults= resultRepo.saveAll(results);
        resultRepo.flush();
        return savedResults;
    }


    @Override
    public void update(Result result) {
        resultRepo.save(result);
    }

    @Override
    public void delete(Result result) {
        resultRepo.delete(result);
    }

    @Override
    public List<Result> findAll() {
        return resultRepo.findAll();
    }

    @Override
    public List<Object[]> getStudentMarkByClassIdAndSubjectId(Long subjectId, Long classId) {
        return resultRepo.getStudentMarkByClassIdAndSubjectId(subjectId, classId);
    }

    @Override
    public Result findResultByUserIdAndSubjectId(Long subjectId, Long userId) {
        return resultRepo.findResultByUserIdAndSubjectId(subjectId, userId);
    }

    @Override
    public List<Result> findUserResults(Long userId) {
        return resultRepo.findUserResults(userId);
    }
}
