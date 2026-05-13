package com.example.demo.mybatisplus;

import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MybatisplusDemoApplicationTests {

    @Autowired
    private EmployeeService employeeService;

    @Test
    void contextLoads() {
        List<Employee> list = employeeService.list();
        list.forEach(System.out::println);

        List<Employee> listAllByLastName = employeeService.listAllByLastName("tom");
        listAllByLastName.forEach(System.out::println);
    }
}
