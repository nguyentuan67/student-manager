package com.vn.studentmanager.controller;

import com.vn.studentmanager.entities.Result;
import com.vn.studentmanager.entities.User;
import com.vn.studentmanager.model.ResultDto;
import com.vn.studentmanager.service.ResultService;
import com.vn.studentmanager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/result")
public class ResultController {

    Logger logger = LoggerFactory.getLogger(ClassroomController.class);
    @Autowired
    UserService userService;
    @Autowired
    ResultService resultService;

    @PostMapping("/update")
    public String saveResults(@ModelAttribute ResultDto form, Model model,
                              @RequestParam("classId") Long classId) {
        try {
            form.getResults().forEach(
                    resultForm -> {
                        Result result = resultService.findResultByUserIdAndSubjectId(resultForm.getSubject().getId(), resultForm.getStudent().getId());
                        if(result == null) {
                            resultForm.setUpdateAt(new Date());
                            resultService.create(resultForm);
                        } else {
                            if(!resultForm.getMark().equals(result.getMark())) {
                                result.setUpdateAt(new Date());
                                result.setMark(resultForm.getMark());
                                resultService.update(result);
                            }
                        }
                    }
            );
//            resultService.saveAll(form.getResults());
        } catch (Exception e) {
            logger.error("", e);
        }
        return "redirect:/classroom/"+classId+"/detail";
    }
}
