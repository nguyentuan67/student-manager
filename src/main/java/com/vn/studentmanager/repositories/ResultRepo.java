package com.vn.studentmanager.repositories;

import com.vn.studentmanager.entities.Result;
import com.vn.studentmanager.model.MarkModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepo extends JpaRepository<Result, Long> {
    @Query(value = "select r.marks, r.update_at, u.name, u.user_id  \n" +
            "from user_class uc \n" +
            "inner join `user` u on uc.user_id = u.user_id\n" +
            "left join result r on uc.user_id = r.student_id and r.subject_id = :subjectId\n" +
            "    where uc.classroom_id = :classId", nativeQuery = true)
    List<Object[]> getStudentMarkByClassIdAndSubjectId(@Param("subjectId") Long subjectId, @Param("classId") Long classId);
    @Query(value = "select r from Result r where r.subject.id = :subjectId and r.student.id = :userId")
    Result findResultByUserIdAndSubjectId(@Param("subjectId") Long subjectId, @Param("userId") Long userId);

    @Query(value = "select r from Result r where r.student.id = :userId")
    List<Result> findUserResults(@Param("userId") Long userId);
}
