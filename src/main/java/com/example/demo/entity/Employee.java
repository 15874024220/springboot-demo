package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tbl_employee")
public class Employee {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String lastName;
    private String email;
    private Integer age;
    @TableField(fill = FieldFill.INSERT) // 插入的时候自动填充
    private LocalDateTime gmtCreate;
    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新的时候自动填充
    private LocalDateTime gmtModified;

    /**
     * 逻辑删除属性
     */
//    @TableLogic
    @TableField("is_deleted")
    private Boolean deleted;

    /**
     * 声明版本号属性，对于在mapper.xml中自定义的sql，无法拦截在where条件中添加，需要是接口自带的方法。
     * 这时需要自己在xml文件的sql语句中显示书写version的where条件
      */
    @Version
    private Integer version;
}