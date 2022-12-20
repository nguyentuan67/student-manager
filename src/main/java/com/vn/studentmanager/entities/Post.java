package com.vn.studentmanager.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    public String getStringDate(Date date) {
        if(date!=null) {
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
            String StrDate = formatter1.format(date);
            return StrDate;
        }
        return "";
    }
}
