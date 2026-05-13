package com.example.demo.util;

import com.example.demo.annotation.Region;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;

/**
 * 案例一:校验特定字段的值是否在可选范围
 * 比如我们现在多了这样一个需求：Person类多了一个 region 字段，
 * region 字段只能是China、China-Taiwan、China-HongKong这三个中的一个。
 *
 * 使用注解：
 *      @Region
 *      private String region;
 */
public class RegionValidator implements ConstraintValidator<Region, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        HashSet<Object> regions = new HashSet<>();
        regions.add("China");
        regions.add("China-Taiwan");
        regions.add("China-HongKong");
        return regions.contains(value);
    }
}
