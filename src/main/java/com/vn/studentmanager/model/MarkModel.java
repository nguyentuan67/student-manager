package com.vn.studentmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkModel {
    @Nullable
    private Float marks;
    private Long userId;
    private String name;
    @Nullable
    private String updateDate;

    public MarkModel(Object[] obj) {
        this.marks = obj[0] == null? null : Float.parseFloat(obj[0].toString());
        this.userId = obj[3] == null? null : Long.parseLong(obj[3].toString());
        this.name = obj[2] == null? null : obj[2].toString();
        this.updateDate = obj[1] == null? null : obj[1].toString();
    }
}
