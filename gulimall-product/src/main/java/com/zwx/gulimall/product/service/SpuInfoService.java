package com.zwx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zwx.common.utils.PageUtils;
import com.zwx.gulimall.product.entity.SpuInfoDescEntity;
import com.zwx.gulimall.product.entity.SpuInfoEntity;
import com.zwx.gulimall.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author zwx
 * @email coderzwx66@163.com
 * @date 2021-01-03 12:40:38
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 商品信息保存
     */
    void saveSpuInfo(SpuSaveVo vo);

    void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity);

    PageUtils queryPageByCondition(Map<String, Object> params);

    /**
     * 商品上架
     * @param spuId spu商品id
     */
    void up(Long spuId);
}

