package com.vn.studentmanager.controller;

import com.vn.studentmanager.entities.*;
import com.vn.studentmanager.model.MarkModel;
import com.vn.studentmanager.model.ResultDto;
import com.vn.studentmanager.service.*;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/classroom")
public class ClassroomController {
    Logger logger = LoggerFactory.getLogger(ClassroomController.class);
    @Autowired
    UserService userService;
    @Autowired
    ResultService resultService;
    @Autowired
    ClassroomService classroomService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    ClassSubjectService classSubjectService;

    @RequestMapping(value = {""}, method = {RequestMethod.GET})
    public String allClass(Model model, Authentication authentication) {
        User authUser = (User) authentication.getPrincipal();
        User authTeacher = userService.findById(authUser.getId());

        List<Classroom> teacherClass = classroomService.findClassByTeacherId(authTeacher.getId());
        List<Classroom> classrooms = classroomService.findAll();

        List<GrantedAuthority> lstAuthorities = new ArrayList<GrantedAuthority>(authentication.getAuthorities());
        for (GrantedAuthority grantedAuthority : lstAuthorities) {
            if (grantedAuthority.getAuthority().equals("TEACHER")) {
                model.addAttribute("classrooms", teacherClass);
            } else {
                model.addAttribute("classrooms", classrooms);
            }
        }
        model.addAttribute("auth", authTeacher);
        return "classroom/list";
    }

    @RequestMapping(value = {"/{id}"}, method = {RequestMethod.GET})
    public String getClassroom(Model model,
                               Authentication authentication,
                               @PathVariable("id") Long id) {
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());

        Classroom classroom = classroomService.findById(id);
        List<User> teachers = userService.findUserWithRole(1);

        List<ClassSubject> classSubjects = classSubjectService.findByClassId(id);
        List<Subject> subjects = new ArrayList<>();
        classSubjects.forEach(classS -> subjects.add(subjectService.findById(classS.getSubjectId())));

        model.addAttribute("auth", authUser);
        model.addAttribute("teachers", teachers);
        model.addAttribute("classroom", classroom);
        model.addAttribute("subjects", subjects);
        return "classroom/detail";
    }

    //classroom page của teacher
    @RequestMapping(value = {"/{id}/detail"}, method = {RequestMethod.GET})
    public String getClassroomDetail(Model model, Authentication authentication,
                                     @PathVariable("id") Long classId) {
        try {
            User authUser = (User) authentication.getPrincipal();
            User authTeacher = userService.findById(authUser.getId());
            Subject subject = authTeacher.getSubjects().get(0);
            Classroom classroom = classroomService.findById(classId);

            List<Result> results = new ArrayList<>();
            resultService.findAll().iterator().forEachRemaining(results::add);

            List<Object[]>studentMarks = resultService.getStudentMarkByClassIdAndSubjectId(subject.getId(), classId);
            List<MarkModel> out = new ArrayList<>();
            for (Object[] obj: studentMarks) {
                out.add(new MarkModel(obj));
            }
            model.addAttribute("auth", authTeacher);
            model.addAttribute("studentMarks", out);
            model.addAttribute("form", new ResultDto(results));
            model.addAttribute("subject", subject);
            model.addAttribute("classroom", classroom);
        } catch (Exception e) {
            logger.error("", e);
        }
        return "classroom/detail";
    }

    //Classroom page của student
    @RequestMapping(value = {"/detail"}, method = {RequestMethod.GET})
    public String getClassInfo(Model model, Authentication authentication) {
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());

        Classroom classroom = classroomService.findById(authUser.getClassrooms().get(0).getId());
        List<ClassSubject> classSubjects = classSubjectService.findByClassId(classroom.getId());
        List<Subject> subjects = new ArrayList<>();
        classSubjects.forEach(classS -> subjects.add(subjectService.findById(classS.getSubjectId())));

        model.addAttribute("auth", authUser);
        model.addAttribute("classroom", classroom);
        model.addAttribute("subjects", subjects);
        return "classroom/detail";
    }

    @RequestMapping(value = {"/{id}/subject/add"}, method = {RequestMethod.GET})
    public String addSubjectGet(Model model,
                                Authentication authentication,
                                @PathVariable("id") Long id) {
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());

        Classroom classroom = classroomService.findById(id);
        List<Subject> subjects = classroomService.subjectLevelClass(classroom.getGrade());
        List<User> teachers = new ArrayList<>();
        subjects.forEach(subject -> teachers.addAll(subject.getUsers()));

        model.addAttribute("auth", authUser);
        model.addAttribute("classroom", classroom);
        model.addAttribute("teachers", teachers);
        return "classroom/addSubject";
    }

    @RequestMapping(value = {"/subject/add"}, method = {RequestMethod.POST})
    public String addSubjectPost(Model model,
                                 @RequestParam(value = "classroomId") Long classroomId,
                                 @RequestParam(value = "teacherId") Long teacherId ) {
        try {
            Classroom classroom = classroomService.findById(classroomId);
            if (classroom == null) {
                return "redirect:/";
             } else {
                User teacher = userService.findById(teacherId);
                Subject newSubject = teacher.getSubjects().get(0);

                //them student vao result
                classroom.getUsers().forEach( user -> {
                        Result result = new Result();
                        result.setStudent(user);
                        result.setSubject(newSubject);
                        result.setUpdateAt(new Date());
                        resultService.create(result);
                    }
                );

                ClassSubject classSubject = new ClassSubject();
                classSubject.setClassroomId(classroomId);
                classSubject.setTeacherId(teacherId);
                classSubject.setSubjectId(newSubject.getId());
                classSubjectService.create(classSubject);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return "redirect:/classroom/"+classroomId;
    }

    @RequestMapping(value = {"/create"}, method = {RequestMethod.GET})
    public String createClassroomGet(Model model,
                                     Authentication authentication) {
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());
        model.addAttribute("auth", authUser);
        return "classroom/create";
    }

    @RequestMapping(value = {"/create"}, method = {RequestMethod.POST})
    public String createClassroomPost(Model model,
                                 @RequestParam(value = "name", defaultValue = "") String name,
                                 @RequestParam(value = "grade", defaultValue = "") int grade,
                                 @RequestParam(value = "amount", defaultValue = "") int amount) {
        try {

            Classroom classroom = new Classroom();
            if (name!=null || name.trim().length() != 0
                    && grade != 0
                    && amount !=0) {
                classroom.setName(name);
                classroom.setGrade(grade);
                classroom.setAmount(amount);
                classroomService.create(classroom);
            } else {
                String log = "Phai nhap day du";
                return "classroom/create";
            }
        } catch (Exception e) {
            logger.error("",e);
        }
        return "redirect:/classroom";
    }

    @RequestMapping(value = {"/{id}/update"}, method = {RequestMethod.GET})
    public String updateClassrommGet(Model model,
                                     Authentication authentication,
                                     @PathVariable("id") Long id) {
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());

        Classroom classroom = classroomService.findById(id);
        model.addAttribute("classroom", classroom);
        model.addAttribute("auth", authUser);
        return "classroom/update";
    }

    @RequestMapping(value = {"/update"}, method = {RequestMethod.POST})
    public String updateClassroomPost(Model model,
                                      @RequestParam(value = "id", defaultValue = "") Long id,
                                      @RequestParam(value = "name", defaultValue = "") String name,
                                      @RequestParam(value = "grade", defaultValue = "") int grade,
                                      @RequestParam(value = "amount", defaultValue = "") int amount) {
        try {
            Classroom classroom = classroomService.findById(id);
            if (classroom == null) {
                return "redirect:/classroom/";
            }
            if (name!=null || name.trim().length() != 0
                    && grade != 0
                    && amount !=0 ) {
                classroom.setName(name);
                classroom.setGrade(grade);
                classroom.setAmount(amount);
                classroomService.update(classroom);
            } else {
                return "classroom/update";
            }
        } catch (Exception e) {
            logger.error("",e);
        }
        return "redirect:/classroom/";
    }

    @RequestMapping(value = {"/find"}, method = {RequestMethod.GET})
    public String findClassroom(Model model,
                              Authentication authentication,
                              @RequestParam(value = "key", defaultValue = "") String key
    ) {
        User auth = (User) authentication.getPrincipal();
        User authUser = userService.findById(auth.getId());
        model.addAttribute("auth", authUser);
        try {
            if(key!=null) {
                List<Classroom> classrooms = classroomService.findByName(key);
                model.addAttribute("classrooms", classrooms);
                return "classroom/list";
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return "classroom/list";
    }

    @RequestMapping(value = {"/delete"}, method = {RequestMethod.GET})
    public String deleteClassroom(Model model,
                                @RequestParam(value = "id", defaultValue = "") Long id) {
        try {
            Classroom classroom = classroomService.findById(id);
            List<ClassSubject> classSubjects = classSubjectService.findByClassId(id);

            //xóa classSubject liên quan
            classSubjects.forEach(classSubject -> classSubjectService.delete(classSubject));
            classroomService.delete(classroom);
        } catch (Exception e) {
            logger.error("", e);
        }
        return "redirect:/classroom/";
    }

    //delete subject in classroom
    @RequestMapping(value = {"/subject/delete"}, method = RequestMethod.GET)
    public String deleteSubjectInClass(@RequestParam(value = "classId") Long classId,
                                       @RequestParam(value = "subjectId") Long subjectId ) {
        try {
            ClassSubject classSubject = classSubjectService.findByClassIdAndSubjectId(classId, subjectId);
            Classroom classroom = classroomService.findById(classId);
            if(classSubject == null) {
                return "redirect:/classroom/";
            } else {
                classroom.getUsers().forEach( user -> {
                    if (resultService.findResultByUserIdAndSubjectId(subjectId, user.getId()) != null) {
                        resultService.delete(resultService.findResultByUserIdAndSubjectId(subjectId, user.getId()));
                    }
                });
            }
            classSubjectService.delete(classSubject);
        } catch (Exception e) {
            logger.error("", e);
        }

        return "redirect:/classroom/"+classId;
    }

}
