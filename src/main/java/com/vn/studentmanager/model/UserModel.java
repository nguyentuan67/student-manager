package com.vn.studentmanager.model;

import com.vn.studentmanager.entities.Classroom;
import com.vn.studentmanager.entities.Role;
import com.vn.studentmanager.entities.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private Long id;

    private String username;

    private String name;

    private String email;

    private String password;

    private String dob;

    private String gender;

    private String address;

    private String phone;

    private List<Integer> roleId;
    @Nullable
    private Long classId;
    @Nullable
    private Long subjectId;

}
