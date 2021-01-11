package com.zwx.gulimall.product.feign;

import com.zwx.common.to.SkuReductionTo;
import com.zwx.common.to.SpuBoundTo;
import com.zwx.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author coderZWX
 * @date 2021-01-07 19:10
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);

}
