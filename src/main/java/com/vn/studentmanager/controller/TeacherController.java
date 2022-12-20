package com.vn.studentmanager.controller;

import com.vn.studentmanager.entities.ClassSubject;
import com.vn.studentmanager.entities.Classroom;
import com.vn.studentmanager.entities.Subject;
import com.vn.studentmanager.entities.User;
import com.vn.studentmanager.service.ClassSubjectService;
import com.vn.studentmanager.service.ClassroomService;
import com.vn.studentmanager.service.SubjectService;
import com.vn.studentmanager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {
    Logger logger = LoggerFactory.getLogger(TeacherController.class);

    @Autowired
    UserService userService;
    @Autowired
    ClassroomService classroomService;
    @Autowired
    ClassSubjectService classSubjectService;
    @Autowired
    SubjectService subjectService;

    @RequestMapping(value = {""}, method = {RequestMethod.GET})
    public String allStudent(Model model, Authentication authentication) {
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());
        List<User> users = userService.findUserWithRole(1);
        model.addAttribute("auth", authUser);
        model.addAttribute("teachers", users);
        return "teacher/list";
    }

    @RequestMapping(value = {"/{id}"}, method = {RequestMethod.GET})
    public String getStudent(Model model,
                             Authentication authentication,
                             @PathVariable("id") Long id) {
        User user = userService.findById(id);
        try {
            List<Classroom> classrooms = new ArrayList<>();
            List<Long> classIds = classSubjectService.findClassIdByTeacherId(id);
            classIds.forEach(classId -> classrooms.add(classroomService.findById(classId)));

            User auth = (User) authentication.getPrincipal();
            User authUser = userService.findById(auth.getId());
            model.addAttribute("auth", authUser);
            model.addAttribute("teacher", user);
            model.addAttribute("classrooms", classrooms);
        } catch (Exception e) {
            logger.error("", e);
        }
        return "teacher/detail";
    }

    @RequestMapping(value = {"/detail"}, method = {RequestMethod.GET})
    public String getInfo(Model model, Authentication authentication) {
        User authUser = (User) authentication.getPrincipal();
        User authTeacher = userService.findById(authUser.getId());

        List<Classroom> classrooms = new ArrayList<>();
        List<Long> classIds = classSubjectService.findClassIdByTeacherId(authUser.getId());
        classIds.forEach(classId -> classrooms.add(classroomService.findById(classId)));

        model.addAttribute("teacher", authTeacher);
        model.addAttribute("classrooms", classrooms);
        return "teacher/detail";
    }

}
