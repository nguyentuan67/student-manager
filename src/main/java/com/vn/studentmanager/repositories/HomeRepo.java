package com.vn.studentmanager.repositories;

import com.vn.studentmanager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeRepo extends JpaRepository<User, Integer> {
}
