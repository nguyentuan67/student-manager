package com.vn.studentmanager.controller;

import com.vn.studentmanager.entities.*;
import com.vn.studentmanager.repositories.ResultRepo;
import com.vn.studentmanager.service.ClassroomService;
import com.vn.studentmanager.service.ResultService;
import com.vn.studentmanager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.text.SimpleDateFormat;
import java.util.*;

import java.util.Date;

@Controller
@RequestMapping("/student")
public class StudentController {
    Logger logger = LoggerFactory.getLogger(StudentController.class);
    @Autowired
    UserService userService;
    @Autowired
    ClassroomService classroomService;
    @Autowired
    ResultService resultService;

    @RequestMapping(value = {""}, method = {RequestMethod.GET})
    public String allStudent(Model model, Authentication authentication) {
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());
        model.addAttribute("auth", authUser);
        List<User> users = userService.findUserWithRole(0);
        model.addAttribute("students", users);
        return "student/list";
    }

    @RequestMapping(value = {"/{id}"}, method = {RequestMethod.GET})
    public String getStudent(Model model,
                             Authentication authentication,
                             @PathVariable("id") Long id) {
        User user = userService.findById(id);
        List<Subject> subjects = user.getSubjects();
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());
        model.addAttribute("auth", authUser);

        model.addAttribute("student", user);
        model.addAttribute("subjects", subjects);
        return "student/detail";
    }

    @RequestMapping(value = {"/detail"}, method = {RequestMethod.GET})
    @PreAuthorize(value = "hasAnyAuthority('ADMIN','USER')")
    public String getInfo(Model model, Authentication authentication) {
        User authUser = (User) authentication.getPrincipal();
        User authStudent = userService.findById(authUser.getId());
        model.addAttribute("student", authStudent);
        return "student/detail";
    }

    @RequestMapping(value = {"/result"}, method = {RequestMethod.GET})
    public String getStudentResult(Model model, Authentication authentication) {
        User authUser = (User) authentication.getPrincipal();
        User authStudent = userService.findById(authUser.getId());
        List<Result> results = resultService.findUserResults(authStudent.getId());
        model.addAttribute("auth", authStudent);
        model.addAttribute("results", results);
        return "student/result";
    }


}
