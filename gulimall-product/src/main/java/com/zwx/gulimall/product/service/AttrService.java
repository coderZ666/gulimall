package com.zwx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zwx.common.utils.PageUtils;
import com.zwx.gulimall.product.entity.AttrEntity;
import com.zwx.gulimall.product.vo.AttrGroupRelationVo;
import com.zwx.gulimall.product.vo.AttrResponseVo;
import com.zwx.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author zwx
 * @email coderzwx66@163.com
 * @date 2021-01-03 12:40:39
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存属性，同时保存属性和属性分类关联关系
     * @param attr 前端传来的保存数据
     */
    void saveAttr(AttrVo attr);

    /**
     * 分页、查询条件以及不同分类的动态查询属性集合
     * @param params 分页参数
     * @param catelogId 分类id
     * @param type 类型（基本类型0或销售类型1）
     * @return 查到的属性集合
     */
    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    /**
     * 查询属性，携带完整修改回显信息
     * @param attrId 属性id
     * @return 属性完整信息
     */
    AttrResponseVo getAttrInfo(Long attrId);

    void updateAttr(AttrResponseVo attr);

    /**
     * 根据属性分组id获得所有关联属性
     * @param attrgroupId 属性组id
     * @return 所有关联属性集合
     */
    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRelationVo[] vos);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    /**
     * 在指定的属性id集合中，找到对应类型为可检索的属性，返回所有找到的id
     * @param attrIds 给定的属性id集合
     * @return 可检索的属性id集合
     */
    List<Long> selectSearchAttrIds(List<Long> attrIds);
}


