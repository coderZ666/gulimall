package com.zwx.common.vaild;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @author coderZWX
 * @date 2021-01-05 20:09
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {

    private Set<Integer> set = new HashSet<>();
    // 初始化方法
    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] vals = constraintAnnotation.vals();
        for(int val : vals) {
            // 将结果添加到set集合
            set.add(val);
        }
    }
    /**
     * 判断效验是否成功
     * @param value 需要效验的值
     * @param context
     * @return
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        // 判断是包含该值
        return set.contains(value);
    }
}
