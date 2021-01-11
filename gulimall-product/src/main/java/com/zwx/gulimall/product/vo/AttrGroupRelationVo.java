package com.zwx.gulimall.product.vo;

import lombok.Data;

/**
 * 接收前端传来的删除属性和属性组关联关系的数据
 * @author coderZWX
 * @date 2021-01-06 20:37
 */
@Data
public class AttrGroupRelationVo {

    /**
     * 属性id
     */
    private Long attrId;

    /**
     * 属性组id
     */
    private Long attrGroupId;

}
