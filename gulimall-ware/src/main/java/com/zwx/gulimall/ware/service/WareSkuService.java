package com.zwx.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zwx.common.to.SkuHasStockTo;
import com.zwx.common.utils.PageUtils;
import com.zwx.gulimall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author zwx
 * @email coderzwx66@163.com
 * @date 2021-01-03 15:20:00
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockTo> getSkusHasStock(List<Long> skuIds);
}

