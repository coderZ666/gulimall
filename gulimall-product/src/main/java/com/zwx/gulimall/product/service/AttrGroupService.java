package com.zwx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zwx.common.utils.PageUtils;
import com.zwx.gulimall.product.entity.AttrGroupEntity;
import com.zwx.gulimall.product.vo.AttrGroupWithAttrsVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author zwx
 * @email coderzwx66@163.com
 * @date 2021-01-03 12:40:39
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    /**
     * 根据分类id查出所有属性分组，每个属性分组中封装上该分组的所有属性
     * @param catelogId 分类id
     * @return 结果集
     */
    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId);
}

