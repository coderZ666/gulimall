package com.zwx.gulimall.product.dao;

import com.zwx.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author zwx
 * @email coderzwx66@163.com
 * @date 2021-01-03 12:40:38
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
