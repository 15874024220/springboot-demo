package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tbl_employee")
public class Employee {

    private Long id;
    private String lastName;
    private String email;
    private Integer age;
}