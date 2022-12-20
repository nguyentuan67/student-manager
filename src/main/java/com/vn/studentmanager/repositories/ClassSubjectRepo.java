package com.vn.studentmanager.repositories;

import com.vn.studentmanager.entities.ClassSubject;
import com.vn.studentmanager.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassSubjectRepo extends JpaRepository<ClassSubject, Long> {
    @Query("select s from ClassSubject s where  (s.classroomId = :classId)")
    List<ClassSubject> findByClassId(@Param("classId") Long classId);
    @Query("select s from ClassSubject s where  (s.teacherId = :teacherId)")
    List<ClassSubject> findByTeacherId(@Param("teacherId") Long teacherId);
    @Query("select s from ClassSubject s where  (s.subjectId = :subjectId)")
    List<ClassSubject> findBySubjectId(@Param("subjectId") Long subjectId);
    @Query(value = "select c.classroom_id from class_subject c inner join user_subject s on c.teacher_id = s.user_id where c.teacher_id = :teacherId", nativeQuery = true)
    List<Long> findClassIdByTeacherId(@Param("teacherId") Long teacherId);
    @Query(value = "select c from ClassSubject c where(c.classroomId = :classId and c.subjectId = :subjectId)")
    ClassSubject findByClassIdAndSubjectId(@Param("classId") Long classId, @Param("subjectId") Long subjectId);
}
