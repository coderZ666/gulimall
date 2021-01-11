package com.zwx.gulimall.search.controller;

import com.zwx.common.exception.BizCodeEnum;
import com.zwx.common.to.es.SkuEsModelTo;
import com.zwx.common.utils.R;
import com.zwx.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author coderZWX
 * @date 2021-01-11 21:09
 */
@Slf4j
@RequestMapping("/search/save")
@RestController
public class ElasticSaveController {

    @Resource
    ProductSaveService productSaveService;

    /**
     * 上架商品
     */
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModelTo> skuEsModels){
        boolean b;
        try {
            b = productSaveService.productStatusUp(skuEsModels);
        } catch (Exception e) {
            log.error("ElasticSaveController商品上架错误{}",e);
            return R.error(
                    BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),
                    BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg()
            );
        }

        if (b){
            return R.ok();
        }

        return R.error(
                BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),
                BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg()
        );
    }

}
