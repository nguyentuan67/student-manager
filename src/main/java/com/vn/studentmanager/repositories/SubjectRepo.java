package com.vn.studentmanager.repositories;

import com.vn.studentmanager.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepo extends JpaRepository<Subject, Long> {
    Subject findTopById(Long id);

    @Query("select s from Subject s where (:key = ' ' or s.name LIKE CONCAT('%', :key, '%'))  ")
    List<Subject> findByName(@Param("key") String key);
}
