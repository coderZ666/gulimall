package com.zwx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zwx.common.utils.PageUtils;
import com.zwx.gulimall.product.entity.ProductAttrValueEntity;

import java.util.Map;

/**
 * spu属性值
 *
 * @author zwx
 * @email coderzwx66@163.com
 * @date 2021-01-03 12:40:39
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

