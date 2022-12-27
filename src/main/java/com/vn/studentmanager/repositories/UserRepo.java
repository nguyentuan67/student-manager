package com.vn.studentmanager.repositories;

import com.vn.studentmanager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findTopById(Long id);
    @Query(value = "select u from User u where(:email = u.email)")
    User findByEmail(@Param("email") String email);
    @Query("select u from User u where (:key = ' ' or u.username LIKE CONCAT('%', :key, '%')) or " +
            "(:key = ' ' or u.name LIKE CONCAT('%', :key, '%'))  ")
        List<User> findUser(@Param("key") String key);

    @Query(value = "SELECT * FROM user u  inner join user_role r on u.user_id = r.user_id WHERE r.role_id = :roleId", nativeQuery = true)
    List<User> findUserWithRole(@Param("roleId") Integer roleId);
}