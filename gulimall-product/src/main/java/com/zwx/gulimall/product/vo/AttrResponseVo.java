package com.zwx.gulimall.product.vo;

import lombok.Data;

/**
 * @author coderZWX
 * @date 2021-01-06 16:47
 */
@Data
public class AttrResponseVo extends AttrVo {
    /**
     * 分类名称
     */
    private String catelogName;
    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 分类id完整父子关系
     */
    private Long[] catelogPath;
}
