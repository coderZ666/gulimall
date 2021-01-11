package com.zwx.gulimall.product.feign;

import com.zwx.common.to.SkuHasStockTo;
import com.zwx.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author coderZWX
 * @date 2021-01-11 20:31
 */
@FeignClient("gulimall-ware")
public interface WareFeignService {

    /**
     * 远程调用设计方法：
     *  1、R设计的时候加上泛型（推荐）
     *  2、直接返回想要的结果
     *  3、自己封装解析结果
     * 查询sku是否有库存
     */
    @PostMapping("/ware/waresku/hasstock")
    R getSkusHasStock(@RequestBody List<Long> skuIds);

}
