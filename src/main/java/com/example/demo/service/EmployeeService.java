package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Employee;

import java.util.List;

public interface EmployeeService extends IService<Employee> {
    List<Employee> listAllByLastName(String lastName);
}
