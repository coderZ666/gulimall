package com.zwx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zwx.common.utils.PageUtils;
import com.zwx.gulimall.product.entity.BrandEntity;
import com.zwx.gulimall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author zwx
 * @email coderzwx66@163.com
 * @date 2021-01-03 12:40:38
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存详细分类品牌关系信息
     * @param categoryBrandRelation 分类品牌管理实体类
     */
    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    /**
     * 根据品牌id和名称，跟新品牌名称
     * @param brandId 品牌id
     * @param name 新的品牌名称
     */
    void updateBrandName(Long brandId, String name);

    /**
     * 根据分类id和名称，跟新分类名称
     * @param catId 分类id
     * @param name 新的分类名称
     */
    void updateCategoryName(Long catId, String name);

    List<BrandEntity> getBrandsByCatId(Long catId);
}

