package com.vn.studentmanager.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.vn.studentmanager.entities.*;
import com.vn.studentmanager.model.PasswordFormModel;
import com.vn.studentmanager.model.UserModel;
import com.vn.studentmanager.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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
    ResultService resultService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = {"", "/list"}, method = RequestMethod.GET)
    public String listUser(Model model, Authentication authentication) {
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("auth", authUser);
        return "user/list";
    }

    @RequestMapping(value = {"/find"}, method = RequestMethod.GET)
    public String findUser(Model model, Authentication authentication,
                           @RequestParam(value = "key") String key) {
        try {
            if (key != null) {
                User authUser = (User) authentication.getPrincipal();
                User auth = userService.findById(authUser.getId());
                List<User> users = userService.findByNameOrUsername(key);
                model.addAttribute("users", users);
                model.addAttribute("auth", auth);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return "user/list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addUserGet(Model model, Authentication authentication) {
        UserModel userModel = new UserModel();
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());

        model.addAttribute("auth", authUser);
        model.addAttribute("userModel", userModel);
        model.addAttribute("classrooms", classroomService.findAll());
        model.addAttribute("subjects", subjectService.findAll());
        return "user/add";
    }

    @RequestMapping(value = {"/add"}, method = {RequestMethod.POST})
    public String addUserPost(Model model,
                              @ModelAttribute @Valid UserModel userModel,
                              BindingResult result,
                              RedirectAttributes redirectAttrs) {
        User user = new User();
        user.setUsername(userModel.getUsername());
        //check username exist
        User userValidate = userService.findByUsername(userModel.getUsername());
        if (userValidate != null) {
            result.rejectValue("username", "");
        }
        //check email exist
        User emailValidate = userService.findByEmail(userModel.getEmail());
        if (emailValidate != null) {
            result.rejectValue("email", "");
        }
        if (result.hasErrors()) {
            model.addAttribute("userModel", userModel);
            model.addAttribute("classrooms", classroomService.findAll());
            model.addAttribute("subjects", subjectService.findAll());
            return "user/add";
        } else {
            try {
                if (userModel.getClassId() != null) {
                    List<Classroom> classrooms = new ArrayList<>();
                    Classroom classroom = classroomService.findById(userModel.getClassId());
                    classrooms.add(classroom);
                    user.setClassrooms(classrooms);
                } else if (userModel.getSubjectId() != null) {
                    List<Subject> subjects = new ArrayList<>();
                    subjects.add(subjectService.findById(userModel.getSubjectId()));
                    user.setSubjects(subjects);
                }
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
                Date newDob = formatter1.parse(userModel.getDob());

                List<Role> newRoles = new ArrayList<>();
                Role role = roleService.findById(userModel.getRoleId().get(0));
                newRoles.add(role);


                user.setPassword(passwordEncoder.encode(userModel.getPassword()));
                user.setName(userModel.getName());
                user.setEmail(userModel.getEmail());
                user.setGender(userModel.getGender());
                user.setPhone(userModel.getPhone());
                user.setAddress(userModel.getAddress());
                user.setDob(newDob);
                user.setRoles(newRoles);

                userService.create(user);
                if (user.getClassrooms() != null) {
                    Classroom classroom = classroomService.findById(userModel.getClassId());
                    //thêm student vào bảng result
                    classroom.getSubjects().forEach(subject -> {
                        Result resultSubject = new Result();
                        resultSubject.setStudent(user);
                        resultSubject.setSubject(subject);
                        resultSubject.setUpdateAt(new Date());
                        resultService.create(resultSubject);
                    });
                }
                redirectAttrs.addFlashAttribute("success", "Đã tạo thành công" + userModel.getUsername());
            } catch (Exception e) {
                logger.error("", e);
                redirectAttrs.addFlashAttribute("warning", "Không thành công");
            }
        }
        return "redirect:/user/add";
    }

    @RequestMapping(value = {"/{id}/update"}, method = {RequestMethod.GET})
    public String updateUserGet( Model model,  @PathVariable("id") Long id) {
        UserModel userModel = new UserModel();
        User user = userService.findById(id);
        model.addAttribute("userModel", userModel);
        model.addAttribute("user", user);
        model.addAttribute("classrooms", classroomService.findAll());
        model.addAttribute("subjects", subjectService.findAll());
        return "user/update";
    }

    @RequestMapping(value = {"/update"}, method = {RequestMethod.POST})
    public String updateUserPost(Model model,
                                 @ModelAttribute UserModel userModel) {
        try {
            User user = userService.findById(userModel.getId());
            if (user == null) {
                return "redirect:/user/list/";
            }
            if (userModel.getClassId() != null) {
                user.getSubjects().clear();
                List<Classroom> classrooms = new ArrayList<>();
                Classroom classroom = classroomService.findById(userModel.getClassId());
                classrooms.add(classroom);
                user.setClassrooms(classrooms);
            } else if (userModel.getSubjectId() != null) {
                List<Subject> subjects = new ArrayList<>();
                subjects.add(subjectService.findById(userModel.getSubjectId()));
                user.setSubjects(subjects);
            }

            SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
            Date newDob = formatter1.parse(userModel.getDob());

            List<Role> newRoles = new ArrayList<>();
            Role role = roleService.findById(userModel.getRoleId().get(0));
            newRoles.add(role);

            user.setName(userModel.getName());
            user.setGender(userModel.getGender());
            user.setPhone(userModel.getPhone());
            user.setAddress(userModel.getAddress());
            user.setDob(newDob);
            user.setRoles(newRoles);
            userService.update(user);
        } catch (Exception e) {
            logger.error("", e);
        }
        return "redirect:/user/list";
    }

    @RequestMapping(value = {"/setting"}, method = {RequestMethod.GET})
    public String authSettingGet(Model model, Authentication authentication) {
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());
        PasswordFormModel passwordFormModel = new PasswordFormModel();

        model.addAttribute("form", passwordFormModel);
        model.addAttribute("auth", authUser);
        return "/user/setting";
    }

    @RequestMapping(value = {"/setting"}, method = {RequestMethod.POST})
    public String authSettingPost(Model model, Authentication authentication,
                                  @ModelAttribute("passwordFormModel") @Valid PasswordFormModel passwordFormModel,
                                  BindingResult result,
                                  RedirectAttributes redirectAttrs ) {
        User auth = (User) authentication.getPrincipal();
        User user = userService.findById(auth.getId());
        Boolean isMatches = passwordEncoder.matches(passwordFormModel.getCurrentPassword(), user.getPassword());
        if(!isMatches) {
            result.rejectValue("currentPassword", "");
        }
        if (result.hasErrors()) {
            model.addAttribute("err", "Mật khẩu không chính xác");
            model.addAttribute("form", passwordFormModel);
            model.addAttribute("auth", user);
            return "user/setting";
        } else {
            try {
                user.setPassword(passwordEncoder.encode(passwordFormModel.getNewPassword()));
                userService.update(user);
                redirectAttrs.addFlashAttribute("success", "Đổi mật khẩu thành công");
            } catch (Exception e) {
                logger.error("", e);
            }
            return "redirect:/user/setting";
        }
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

}
