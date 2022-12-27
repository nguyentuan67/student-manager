package com.vn.studentmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordFormModel {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
