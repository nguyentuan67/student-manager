package com.vn.studentmanager.repositories;

import com.vn.studentmanager.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {
    @Query(value = "select p from Post p where (p.description LIKE CONCAT('%', :key, '%') or p.title LIKE CONCAT('%', :key, '%'))")
    List<Post> findByKey(@Param("key") String key);
}
