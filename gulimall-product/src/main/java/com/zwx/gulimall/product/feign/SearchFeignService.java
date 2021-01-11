package com.zwx.gulimall.product.feign;

import com.zwx.common.to.es.SkuEsModelTo;
import com.zwx.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author coderZWX
 * @date 2021-01-11 21:57
 */
@FeignClient("gulimall-search")
public interface SearchFeignService {

    /**
     * 上架商品
     */
    @PostMapping("/search/save/product")
    public R productStatusUp(@RequestBody List<SkuEsModelTo> skuEsModels);

}
