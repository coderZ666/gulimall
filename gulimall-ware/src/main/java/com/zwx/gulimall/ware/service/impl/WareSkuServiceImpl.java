package com.zwx.gulimall.ware.service.impl;

import com.zwx.common.to.SkuHasStockTo;
import com.zwx.common.utils.R;
import com.zwx.gulimall.ware.feign.ProductFeignService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zwx.common.utils.PageUtils;
import com.zwx.common.utils.Query;

import com.zwx.gulimall.ware.dao.WareSkuDao;
import com.zwx.gulimall.ware.entity.WareSkuEntity;
import com.zwx.gulimall.ware.service.WareSkuService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Resource
    WareSkuDao wareSkuDao;

    @Resource
    ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        String wareId = (String) params.get("wateId");
        if (!StringUtils.isEmpty(skuId)){
            queryWrapper.eq("sku_id",skuId);
        }
        if (!StringUtils.isEmpty(wareId)){
            queryWrapper.eq("ware_id",wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        //1、判断如果还没有这个库存记录新增
        List<WareSkuEntity> entities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if(entities == null || entities.size() == 0){
            WareSkuEntity skuEntity = new WareSkuEntity();
            skuEntity.setSkuId(skuId);
            skuEntity.setStock(skuNum);
            skuEntity.setWareId(wareId);
            skuEntity.setStockLocked(0);
            //TODO 远程查询sku的名字，如果失败，整个事务无需回滚
            //1、自己catch异常
            //TODO 还可以用什么办法让异常出现以后不回滚？高级
            try {
                R info = productFeignService.info(skuId);
                Map<String,Object> data = (Map<String, Object>) info.get("skuInfo");

                if(info.getCode() == 0){
                    skuEntity.setSkuName((String) data.get("skuName"));
                }
            }catch (Exception e){

            }


            wareSkuDao.insert(skuEntity);
        }else{
            //如果是个已存在的库存，直接添加数量即可
            wareSkuDao.addStock(skuId,wareId,skuNum);
        }

    }

    @Override
    public List<SkuHasStockTo> getSkusHasStock(List<Long> skuIds) {
        List<SkuHasStockTo> collect = skuIds.stream().map(skuId -> {
            SkuHasStockTo vo = new SkuHasStockTo();
            //利用sku商品id获取库存信息
            // 查到的可能是同一个商品在多个仓库的库存信息
            // 所以为了判断是否还有剩余库存，需要进行加和
            // 并且还要减去已经锁定的库存，才是真实的现有库存量
            //也就是下面这样的sql语句：查到当前商品的库存量
            // SELECT SUM(stock-stock_locker) FROM `wms_ware_sku` WHERE sku_id=#{skuId}
            Long count = baseMapper.getSkuStock(skuId);

            vo.setSkuId(skuId);
            //库存量大于0表示有库存
            vo.setHasStock(count != null && count > 0);

            return vo;
        }).collect(Collectors.toList());
        return collect;
    }

}