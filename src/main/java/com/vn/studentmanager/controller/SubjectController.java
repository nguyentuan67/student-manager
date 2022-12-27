package com.vn.studentmanager.controller;

import com.vn.studentmanager.entities.ClassSubject;
import com.vn.studentmanager.entities.Subject;
import com.vn.studentmanager.entities.User;
import com.vn.studentmanager.service.ClassSubjectService;
import com.vn.studentmanager.service.SubjectService;
import com.vn.studentmanager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/subject")
public class SubjectController {
    Logger logger = LoggerFactory.getLogger(SubjectController.class);
    @Autowired
    SubjectService subjectService;
    @Autowired
    UserService userService;
    @Autowired
    ClassSubjectService classSubjectService;

    @RequestMapping(value = {""}, method = {RequestMethod.GET})
    public String allSubject(Model model, Authentication authentication) {
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());
        List<Subject> subjects;
        subjects = subjectService.findAll();
        model.addAttribute("auth", authUser);
        model.addAttribute("subjects", subjects);
        return "subject/list";
    }

    @RequestMapping(value = {"/{id}"}, method = {RequestMethod.GET})
    public String getSubject(Model model,
                             Authentication authentication,
                             @PathVariable("id") Long id) {
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());
        model.addAttribute("auth", authUser);
        Subject subject = subjectService.findById(id);
        model.addAttribute("subject", subject);
        return "subject/detail";
    }

    @RequestMapping(value = {"/create"}, method = {RequestMethod.GET})
    public String createSubjectGet(Model model,
                                   Authentication authentication) {
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());
        model.addAttribute("auth", authUser);
        return "subject/create";
    }

    @RequestMapping(value = {"/create"}, method = {RequestMethod.POST})
    public String createSubjectPost(Model model,
                                      @RequestParam(value = "name", defaultValue = "") String name,
                                      @RequestParam(value = "grade", defaultValue = "") int grade,
                                      @RequestParam(value = "description", defaultValue = "") String description) {
        try {
            Subject subject = new Subject();
            if (name!=null || name.trim().length() != 0
                    && grade != 0
                    && description!=null || description.trim().length() !=0) {
                subject.setName(name);
                subject.setGrade(grade);
                subject.setDescription(description);
                subjectService.create(subject);
            } else {
                String log = "Phai nhap day du";
                return "subject/create";
            }
        } catch (Exception e) {
            logger.error("",e);
        }
        return "redirect:/subject";
    }

    @RequestMapping(value = {"/{id}/update"}, method = {RequestMethod.GET})
    public String updateSubjectGet(Model model,
                                   Authentication authentication,
                                   @PathVariable("id") Long id) {
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());
        model.addAttribute("auth", authUser);
        Subject subject = subjectService.findById(id);
        model.addAttribute("subject", subject);

        return "subject/update";
    }

    @RequestMapping(value = {"/update"}, method = {RequestMethod.POST})
    public String updateSubjectPost(Model model,
                                      @RequestParam(value = "id", defaultValue = "") Long id,
                                      @RequestParam(value = "name", defaultValue = "") String name,
                                      @RequestParam(value = "grade", defaultValue = "") int grade,
                                      @RequestParam(value = "description", defaultValue = "") String description) {
        try {
            Subject subject = subjectService.findById(id);
            if (subject == null) {
                return "redirect:/subject/";
            }
            if (name!=null || name.trim().length() != 0
                    && grade != 0
                    && description!=null || description.trim().length() !=0 ) {
                subject.setName(name);
                subject.setGrade(grade);
                subject.setDescription(description);
                subjectService.update(subject);
            } else {
                return "subject/update";
            }
        } catch (Exception e) {
            logger.error("",e);
        }
        return "redirect:/subject/";
    }

    @RequestMapping(value = {"/find"}, method = {RequestMethod.GET})
    public String findSubject(Model model,
                              Authentication authentication,
                              @RequestParam(value = "key", defaultValue = "") String key
    ) {
        try {
            if(key!=null) {
                User auth = (User) authentication.getPrincipal();
                User authUser = userService.findById(auth.getId());
                model.addAttribute("auth", authUser);
                List<Subject> subjects = subjectService.findByName(key);
                model.addAttribute("subjects", subjects);
                return "subject/list";
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return "subject/list";
    }

    @RequestMapping(value = {"/delete"}, method = {RequestMethod.GET})
    public String deleteSubject(Model model,
                                  @RequestParam(value = "id", defaultValue = "") Long id) {
        try {
            Subject subject = subjectService.findById(id);
            List<ClassSubject> classSubjects = classSubjectService.findBySubjectId(id);

            //xóa classSubject liên quan
            classSubjects.forEach(classSubject -> classSubjectService.delete(classSubject));
            //xóa subject của teacher

            subjectService.delete(subject);
        } catch (Exception e) {
            logger.error("", e);
        }
        return "redirect:/subject/";
    }

}
