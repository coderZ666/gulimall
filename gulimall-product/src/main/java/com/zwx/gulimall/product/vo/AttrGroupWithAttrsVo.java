package com.zwx.gulimall.product.vo;

import com.zwx.gulimall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @author coderZWX
 * @date 2021-01-07 14:41
 */
@Data
public class AttrGroupWithAttrsVo {

    private static final long serialVersionUID = 1L;

    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    /**
     * 封装分组下的所有属性
     */
    private List<AttrEntity> attrs;

}
