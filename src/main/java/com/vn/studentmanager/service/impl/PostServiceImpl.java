package com.vn.studentmanager.service.impl;

import com.vn.studentmanager.entities.Post;
import com.vn.studentmanager.repositories.PostRepo;
import com.vn.studentmanager.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    PostRepo postRepo;
    @Override
    public void create(Post post) {
        postRepo.save(post);
    }

    @Override
    public void update(Post post) {
        postRepo.save(post);
    }

    @Override
    public void delete(Post post) {
        postRepo.delete(post);
    }

    @Override
    public List<Post> findAll() {
        return postRepo.findAll();
    }

    @Override
    public List<Post> findByTitle(String key) {
        return null;
    }
}
