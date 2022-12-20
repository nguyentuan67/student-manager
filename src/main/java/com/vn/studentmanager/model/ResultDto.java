package com.vn.studentmanager.model;

import com.vn.studentmanager.entities.Result;

import java.util.List;

public class ResultDto {
    private List<Result> results;

    public ResultDto(List<Result> results) {
    }

    public void addResult(Result result) {
        this.results.add(result);
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}
