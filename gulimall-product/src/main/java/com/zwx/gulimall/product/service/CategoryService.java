package com.zwx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zwx.common.utils.PageUtils;
import com.zwx.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author zwx
 * @email coderzwx66@163.com
 * @date 2021-01-03 12:40:38
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查出所有分类以及子分类，以树形结构组装起来
     */
    List<CategoryEntity> listWithTree();

    /**
     * 批量删除菜单
     * @param asList 菜单id的集合
     */
    void removeMenuByIds(List<Long> asList);

    /**
     * 根据所属分类id，查询该分类完整层级父子id数组
     * [父id,子id,孙id]
     * @param catelogId 属性所属分类id
     * @return 分类父子id数组
     */
    Long[] findCatelogPath(Long catelogId);

    /**
     * 级联更新所有关联的数据
     * @param category 封装更新内容的实体
     */
    void updateCascade(CategoryEntity category);
}

