package com.vn.studentmanager.controller;

import com.vn.studentmanager.entities.Post;
import com.vn.studentmanager.entities.User;
import com.vn.studentmanager.service.PostService;
import com.vn.studentmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model, Authentication authentication){
        User authUser = (User) authentication.getPrincipal();
        User user = userService.findById(authUser.getId());
        List<Post> posts = postService.findAll();
        Collections.reverse(posts);
        model.addAttribute("posts", posts);
        model.addAttribute("auth", user);
        return "home";
    }
}
