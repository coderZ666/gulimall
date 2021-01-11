package com.zwx.gulimall.product.service.impl;

import com.zwx.common.utils.PageUtils;
import com.zwx.common.utils.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zwx.gulimall.product.dao.SkuInfoDao;
import com.zwx.gulimall.product.entity.SkuInfoEntity;
import com.zwx.gulimall.product.service.SkuInfoService;
import org.springframework.util.StringUtils;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        /**
         * key: '华为',//检索关键字
         * catelogId: 0,
         * brandId: 0,
         * min: 0,
         * max: 0
         */
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            wrapper.and(w->{
                w.eq("sku_id",key).or().like("sku_name",key);
            });
        }
        String catalogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catalogId) && !"0".equals(catalogId)){
            wrapper.eq("catalog_id",catalogId);
        }
        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equals(brandId)){
            wrapper.eq("brand_id",brandId);
        }
        String min = (String) params.get("min");
        if (!StringUtils.isEmpty(min)){
            wrapper.ge("price",min);
        }
        String max = (String) params.get("max");
        if (!StringUtils.isEmpty(max)){
            try {
                BigDecimal bigDecimal = new BigDecimal(max);
                if (bigDecimal.compareTo(new BigDecimal("0"))==1){
                    wrapper.le("price",max);
                }
            }catch (Exception e){
                log.error("输入的价格信息不是数字");
            }
        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
        List<SkuInfoEntity> list
                = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id",spuId));
        return list;
    }

}