package com.vn.studentmanager.service;

import com.vn.studentmanager.entities.Post;

import java.util.List;

public interface PostService {
    void create(Post post);
    void update(Post post);
    void delete(Post post);
    List<Post> findAll();
    List<Post> findByTitle(String key);
}
