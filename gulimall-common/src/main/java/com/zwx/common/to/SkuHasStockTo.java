package com.zwx.common.to;

import lombok.Data;

/**
 * 保存对应skuId的商品是否有库存的信息
 * @author coderZWX
 * @date 2021-01-11 18:05
 */
@Data
public class SkuHasStockTo {

    /**
     * sku商品的id
     */
    private Long skuId;
    /**
     * 该商品是否有库存
     */
    private boolean hasStock;

}
