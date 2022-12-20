package com.vn.studentmanager.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.vn.studentmanager.entities.Classroom;
import com.vn.studentmanager.entities.Role;
import com.vn.studentmanager.entities.Subject;
import com.vn.studentmanager.entities.User;
import com.vn.studentmanager.model.UserModel;
import com.vn.studentmanager.service.ClassroomService;
import com.vn.studentmanager.service.RoleService;
import com.vn.studentmanager.service.SubjectService;
import com.vn.studentmanager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(StudentController.class);
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    ClassroomService classroomService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = {"", "/list"}, method = RequestMethod.GET)
    public String listUser(Model model, Authentication authentication){
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("auth", authUser);
        return "user/list";
    }

    @RequestMapping(value = {"/find"}, method = RequestMethod.GET)
    public String findUser(Model model,
                           @RequestParam(value = "key") String key ){
        try {
            if(key != null) {
                List<User> users = userService.findByNameOrUsername(key);
                model.addAttribute("users", users);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return "user/list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addUserGet(Model model, Authentication authentication){
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());
        model.addAttribute("auth", authUser);
        model.addAttribute("classrooms", classroomService.findAll());
        model.addAttribute("subjects", subjectService.findAll());
        return "user/add";
    }

    @RequestMapping(value = {"/add"}, method = {RequestMethod.POST})
    public String addUserPost(Model model,
                              @RequestParam(value = "username", defaultValue = "") String username,
                              @RequestParam(value = "password", defaultValue = "") String password,
                              @RequestParam(value = "name", defaultValue = "") String name,
                              @RequestParam(value = "email", defaultValue = "") String email,
                              @RequestParam(value = "dob", defaultValue = "") String dob,
                              @RequestParam(value = "gender", defaultValue = "") String gender,
                              @RequestParam(value = "phone", defaultValue = "") String phone,
                              @RequestParam(value = "address", defaultValue = "") String address,
                              @RequestParam(value = "role_id", defaultValue = "0") Integer roleId,
                              @RequestParam(value = "classroom_id", defaultValue = "") Long classId,
                              @RequestParam(value = "subject_id", defaultValue = "") Long subjectId ) {
        try {
            User user = new User();
            if (    username!=null || username.trim().length() != 0
                    && password!=null || password.trim().length() != 0
                    && name!=null || name.trim().length() != 0
                    && email!=null || email.trim().length() != 0
                    && dob!=null || dob.trim().length() != 0
                    && gender!=null || gender.trim().length() != 0
                    && phone!=null || phone.trim().length() != 0
                    && address!=null || address.trim().length() != 0
                    && roleId != null ) {
                if(classId != null) {
                    List<Classroom> classrooms = new ArrayList<>();
                    classrooms.add(classroomService.findById(classId));
                    user.setClassrooms(classrooms);
                } else if (subjectId != null) {
                    List<Subject> subjects = new ArrayList<>();
                    subjects.add(subjectService.findById(subjectId));
                    user.setSubjects(subjects);
                }
                SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy");
                Date newDob = formatter1.parse(dob);

                List<Role> newRoles = new ArrayList<>();
                Role role = roleService.findById(roleId);
                newRoles.add(role);

                user.setUsername(username);
                user.setPassword(passwordEncoder.encode(password));
                user.setName(name);
                user.setEmail(email);
                user.setGender(gender);
                user.setPhone(phone);
                user.setAddress(address);
                user.setDob(newDob);
                user.setRoles(newRoles);


                userService.create(user);
            } else {
                String log = "Phai nhap day du";
                return "user/add";
            }
        } catch (Exception e) {
            logger.error("",e);
        }
        return "redirect:/user/list";
    }

    @RequestMapping(value = {"/{id}/update"}, method = {RequestMethod.GET})
    public String updateUserGet(
            Model model, Authentication authentication,
            @PathVariable("id") Long id) {
        User user = userService.findById(id);
        List<Subject> subjects = user.getSubjects();
        List<Classroom> classrooms = user.getClassrooms();
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());
        model.addAttribute("auth", authUser);
        model.addAttribute("user", user);
        model.addAttribute("classrooms", classroomService.findAll());
        model.addAttribute("subjects", subjectService.findAll());
        return "user/update";
    }

    @RequestMapping(value = {"/update"}, method = {RequestMethod.POST})
    public String updateUserPost(Model model,
                                @RequestParam(value = "id", defaultValue = "") Long id,
                                @RequestParam(value = "name", defaultValue = "") String name,
                                @RequestParam(value = "dob", defaultValue = "") String dob,
                                @RequestParam(value = "gender", defaultValue = "") String gender,
                                @RequestParam(value = "phone", defaultValue = "") String phone,
                                @RequestParam(value = "address", defaultValue = "") String address,
                                @RequestParam(value = "role_id", defaultValue = "0") Integer roleId,
                                 @RequestParam(value = "classroom_id", defaultValue = "") Long classId,
                                 @RequestParam(value = "subject_id", defaultValue = "") Long subjectId ) {
        try {
            User user = userService.findById(id);
            if (user == null) {
                return "redirect:/user/list/";
            }
            if (    name!=null || name.trim().length() != 0
                    && dob!=null || dob.trim().length() != 0
                    && gender!=null || gender.trim().length() != 0
                    && phone!=null || phone.trim().length() != 0
                    && address!=null || address.trim().length() != 0
                    && roleId != null ) {

                if(classId != null) {
                    user.getSubjects().clear();
                    List<Classroom> classrooms = new ArrayList<>();
                    classrooms.add(classroomService.findById(classId));
                    user.setClassrooms(classrooms);
                } else if (subjectId != null) {
                    user.getClassrooms().clear();
                    List<Subject> subjects = new ArrayList<>();
                    subjects.add(subjectService.findById(subjectId));
                    user.setSubjects(subjects);
                }

                SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy");
                Date newDob = formatter1.parse(dob);

                List<Role> newRoles = new ArrayList<>();
                Role role = roleService.findById(roleId);
                newRoles.add(role);

                user.setName(name);
                user.setGender(gender);
                user.setPhone(phone);
                user.setAddress(address);
                user.setDob(newDob);
                user.setRoles(newRoles);
                userService.update(user);
            } else {
                String log = "Phai nhap day du";
                return "user/update";
            }
        } catch (Exception e) {
            logger.error("",e);
        }
        return "redirect:/user/list";
    }

    @RequestMapping(value = {"/delete"}, method = {RequestMethod.GET})
    public String deleteClassroom(@RequestParam(value = "id", defaultValue = "") Long id) {
        try {
            User user = userService.findById(id);
            userService.delete(user);
        } catch (Exception e) {
            logger.error("", e);
        }
        return "redirect:/user/list/";
    }

//    @RequestMapping(value = {"/result"}, method = {RequestMethod.GET})
//    public String getResultStudent(@RequestParam(value = "id", defaultValue = "") Long id) {
//        try {
//            User user = userService.findById(id);
//            userService.delete(user);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return "redirect:/user/list/";
//    }

}
