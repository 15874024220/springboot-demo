package com.example.demo.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
//        List<Employee> list = employeeService.list();
//        list.forEach(System.out::println);
//
        /**
         * 自定义sql查询
         */
        /*List<Employee> listAllByLastName = employeeService.listAllByLastName("tom");
        listAllByLastName.forEach(System.out::println);
*/
        /**
         * 插入测试
          */
//        Employee employee = new Employee();
//        employee.setLastName("lisa");
//        employee.setEmail("lisa@qq.com");
//        employee.setAge(20);
//        employeeService.save(employee);

        /**
         * 修改测试
          */
        /*Employee employee = new Employee();
        employee.setId(2054408672210714626L);
        employee.setAge(15);
        employeeService.updateById(employee);*/

//        employeeService.removeById(2054407844305547266L);


        /**
         *  查询名字中包含'j'，年龄大于20岁，邮箱不为空的员工信息
         */
        /*LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<Employee>()
                .like(Employee::getLastName,"j")
                .gt(Employee::getAge,20)
                .isNotNull(Employee::getEmail);
        List<Employee> employeeList = employeeService.list(wrapper);
        employeeList.forEach(System.out::println);*/

        /**
         * lambda 修改
         */
        LambdaUpdateWrapper lambdaUpdateWrapper = new LambdaUpdateWrapper<Employee>()
                .set(Employee::getAge, 50)
                .set(Employee::getEmail, "emp@163.com")
                .eq(Employee::getId, 2054477208014741505L)
                .eq(Employee::getVersion, 1);

        employeeService.update(null, lambdaUpdateWrapper);
    }

    @Test
    void pageLoads() {
        Page<Employee> page = new Page<>(1,2);

//        employeeService.page(page, new QueryWrapper<Employee>()
//                .between("age", 20, 50)
//                .eq("gender", 1));
//
//        employeeService.page(page, null);

        employeeService.listAllByCustomCondition(page, 20, 50, 1);


        List<Employee> employeeList = page.getRecords();
        employeeList.forEach(System.out::println);
        System.out.println("获取总条数:" + page.getTotal());
        System.out.println("获取当前页码:" + page.getCurrent());
        System.out.println("获取总页码:" + page.getPages());
        System.out.println("获取每页显示的数据条数:" + page.getSize());
        System.out.println("是否有上一页:" + page.hasPrevious());
        System.out.println("是否有下一页:" + page.hasNext());
    }

    @Test
    void optimisticLock() {
        Employee employee = employeeService.getById(2054476184755613697L);
        employee.setAge(123);
        boolean result = employeeService.updateById(employee);
        System.out.println("结果：" + result);
    }
}
