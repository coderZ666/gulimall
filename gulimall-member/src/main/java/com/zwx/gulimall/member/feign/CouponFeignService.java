package com.zwx.gulimall.member.feign;

import com.zwx.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author coderZWX
 * @date 2021-01-03 16:32
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    @GetMapping("/coupon/coupon/member/list")
    R memberCoupons();

}
