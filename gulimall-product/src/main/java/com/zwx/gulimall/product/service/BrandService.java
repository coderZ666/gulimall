package com.zwx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zwx.common.utils.PageUtils;
import com.zwx.gulimall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author zwx
 * @email coderzwx66@163.com
 * @date 2021-01-03 12:40:38
 */
public interface BrandService extends IService<BrandEntity> {

    /**
     * 查询品牌列表
     * @param params 查询参数，包含分页信息和查询词信息
     * @return 查到的集合
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 修改品牌，同时视情况修改分类品牌关联表中相关的冗余字段
     * 保证各种关联数据的一致性
     * @param brand 修改信息
     */
    void updateDetail(BrandEntity brand);
}

