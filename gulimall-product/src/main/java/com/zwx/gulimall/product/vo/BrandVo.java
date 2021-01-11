package com.zwx.gulimall.product.vo;

import lombok.Data;

/**
 * 返回分类关联的品牌所需参数
 * @author coderZWX
 * @date 2021-01-07 1:23
 */
@Data
public class BrandVo {

    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 品牌名称
     */
    private String brandName;

}
