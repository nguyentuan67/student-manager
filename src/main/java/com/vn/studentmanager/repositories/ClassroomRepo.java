package com.vn.studentmanager.repositories;

import com.vn.studentmanager.entities.Classroom;
import com.vn.studentmanager.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepo extends JpaRepository<Classroom, Long> {
    Classroom findTopById(Long id);

    @Query("select c from Classroom c where (:key = ' ' or c.name LIKE CONCAT('%', :key, '%'))  ")
    List<Classroom> findByName(@Param("key") String key);

    @Query("select s from Subject s where  (s.grade = :grade_class)")
    List<Subject> subjectLevelClass(@Param("grade_class") int gradeClass);
    @Query(value = "SELECT c.* FROM classroom c inner join class_subject s on c.id = s.classroom_id \n" +
            "where s.teacher_id = :teacherId", nativeQuery = true)
    List<Classroom> findClassByTeacherId(@Param("teacherId") Long teacherId);
}
