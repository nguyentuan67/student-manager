package com.vn.studentmanager.controller;

import com.vn.studentmanager.entities.Post;
import com.vn.studentmanager.entities.User;
import com.vn.studentmanager.service.PostService;
import com.vn.studentmanager.service.UserService;
import javafx.geometry.Pos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {
    Logger logger = LoggerFactory.getLogger(PostController.class);
    @Autowired
    PostService postService;
    @Autowired
    UserService userService;

    @RequestMapping(value = {""}, method = {RequestMethod.GET})
    public String getAllPost(Model model, Authentication authentication) {
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());
        List<Post> posts = postService.findAll();
        model.addAttribute("auth", authUser);
        model.addAttribute("posts", posts);
        return "post/list";
    }

    @RequestMapping(value = {"/create"}, method = {RequestMethod.GET})
    public String createPostGet(Model model, Authentication authentication) {
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());
        model.addAttribute("auth", authUser);
        return "post/create";
    }

    @RequestMapping(value = {"/create"}, method = {RequestMethod.POST})
    public String createPostPost(Model model,
                                 @RequestParam(value = "title") String title,
                                 @RequestParam(value = "description") String description) {
        try {
            Post post = new Post();
            if(title!=null && description!=null) {
                post.setTitle(title);
                post.setDescription(description);
                post.setCreatedAt(new Date());
                post.setUpdatedAt(new Date());
                postService.create(post);
            }
        } catch (Exception e) {
            logger.error("", e);
        }

        return "redirect:/post";
    }

    @RequestMapping(value = {"/find"}, method = {RequestMethod.GET})
    public String findPost(Model model,
                           Authentication authentication,
                           @RequestParam(value = "key", defaultValue = "") String key) {
        try {
            if(key!=null) {
                List<Post> posts = postService.findByKey(key);
                User auth = (User) authentication.getPrincipal();
                User authUser = userService.findById(auth.getId());
                Collections.reverse(posts);
                model.addAttribute("auth", authUser);
                model.addAttribute("posts", posts);
                return "home";
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return "post/list";
    }
}
