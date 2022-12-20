package com.vn.studentmanager.service.impl;

import com.vn.studentmanager.repositories.HomeRepo;
import com.vn.studentmanager.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private HomeRepo homeRepo;

}
