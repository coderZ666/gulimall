package com.zwx.gulimall.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.zwx.common.constant.ProductConstant;
import com.zwx.common.to.SkuHasStockTo;
import com.zwx.common.to.SkuReductionTo;
import com.zwx.common.to.SpuBoundTo;
import com.zwx.common.to.es.SkuEsModelTo;
import com.zwx.common.utils.PageUtils;
import com.zwx.common.utils.Query;
import com.zwx.common.utils.R;
import com.zwx.gulimall.product.entity.*;
import com.zwx.gulimall.product.feign.CouponFeignService;
import com.zwx.gulimall.product.feign.SearchFeignService;
import com.zwx.gulimall.product.feign.WareFeignService;
import com.zwx.gulimall.product.service.*;
import com.zwx.gulimall.product.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zwx.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Resource
    SpuInfoDescService spuInfoDescService;
    @Resource
    SpuImagesService spuImagesService;
    @Resource
    AttrService attrService;
    @Resource
    ProductAttrValueService attrValueService;
    @Resource
    SkuInfoService skuInfoService;
    @Resource
    SkuImagesService skuImagesService;
    @Resource
    SkuSaleAttrValueService skuSaleAttrValueService;
    @Resource
    BrandService brandService;
    @Resource
    CategoryService categoryService;

    @Resource
    CouponFeignService couponFeignService;
    @Resource
    WareFeignService wareFeignService;
    @Resource
    SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    /**
     * 商品保存功能
     * TODO 高级部分来完善各种失败后回滚的情况——分布式事务等
     */
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {
        //1、保存spu基本信息；pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo,spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);

        //2、保存spu的描述图片;pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(spuInfoEntity.getId());
        descEntity.setDecript(String.join("|--|",decript));
        spuInfoDescService.saveSpuInfoDesc(descEntity);

        //3、保存spu的图片集;pms_spu_images
        List<String> images = vo.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(),images);

        //4、保存spu的规格参数；pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setAttrId(attr.getAttrId());
            AttrEntity attrEntity = attrService.getById(attr.getAttrId());
            valueEntity.setAttrName(attrEntity.getAttrName());
            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            valueEntity.setSpuId(spuInfoEntity.getId());
            return valueEntity;
        }).collect(Collectors.toList());
        attrValueService.saveProductAttr(collect);

        //5、保存spu的积分信息；gulimall_sms->sms_spu_bounds
        Bounds bounds = vo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds,spuBoundTo);
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if (r.getCode()!=0){
            log.error("远程服务coupon保存spu积分信息失败");
        }

        //6、保存当前spu对应的所有sku信息
        List<Skus> skus = vo.getSkus();
        //6.1 sku的基本信息；pms_sku_info
        if (skus!=null && skus.size()>0){
            skus.forEach(item->{
                String defaultImg="";
                for (Images image : item.getImages()) {
                    if (image.getDefaultImg()==1){
                        defaultImg = image.getImgUrl();
                    }
                }
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item,skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoService.saveSkuInfo(skuInfoEntity);

                Long skuId = skuInfoEntity.getSkuId();


                //6.2 sku的图片信息；pms_sku_images
                List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(image -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(image.getImgUrl());
                    skuImagesEntity.setDefaultImg(image.getDefaultImg());
                    return skuImagesEntity;
                }).filter(entity->{
                    //没有图片路径的无需保存
                    //返回true就是需要，返回false就被过滤剔除掉
                    return StringUtils.isNotEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());
                skuImagesService.saveBatch(imagesEntities);
                //6.3 sku的销售属性信息；pms_sku_sale_attr_value
                List<Attr> attr = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(a -> {
                    SkuSaleAttrValueEntity attrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, attrValueEntity);
                    attrValueEntity.setSkuId(skuId);
                    return attrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                //6.4 sku的优惠、满减等信息；
                // gulimall_sms->sms_sku_ladder/sms_sku_full_reduction/sms_member_price/sms_spu_bounds
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item,skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if (skuReductionTo.getFullCount()>0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0"))==1){
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if (r1.getCode()!=0){
                        log.error("远程服务coupon保存sku优惠信息失败");
                    }
                }
            });
        }
    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();

        /**
         * 判断查询条件key、status、brandId、catelogId不为空则分别拼装查询条件
         */
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            wrapper.and((w)->{
                w.eq("id",key).or().like("spu_name",key);
            });
        }
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)){
            wrapper.eq("publish_status",status);
        }
        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId)){
            wrapper.eq("brand_id",brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId)){
            wrapper.eq("catalog_id",catelogId);
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {

        //1、查出当前spuId对应的所有sku信息
        List<SkuInfoEntity> skus = skuInfoService.getSkusBySpuId(spuId);

        //2、封装每个sku信息
        // 循环封装之前需要一些可以在循环外只查询一次的内容
        // 品牌信息和分类信息在spu下所有sku都是一样的，不用每次循环都查一遍
        BrandEntity brand = brandService.getById(skus.get(0).getBrandId());
        CategoryEntity category = categoryService.getById(skus.get(0).getCatalogId());

        //可检索属性在spu下所有sku都是一样的，不用每次循环都查一遍
        List<ProductAttrValueEntity> baseAttrs = attrValueService.baseAttrListForSpu(spuId);
        List<Long> attrIds = baseAttrs.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        //查出属性中可以检索的属性的id集合
        List<Long> searchAttrIds = attrService.selectSearchAttrIds(attrIds);
        //过滤掉不可检索的属性id，拿到可检索的属性id,再查出具体的属性
        List<SkuEsModelTo.Attrs> attrsList = baseAttrs.stream().filter(item -> {
            return searchAttrIds.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModelTo.Attrs attr = new SkuEsModelTo.Attrs();
            BeanUtils.copyProperties(item, attr);
            return attr;
        }).collect(Collectors.toList());

        //远程调用库存服务统一查询所有的商品各自是否有库存
        List<Long> skuIds = skus.stream()
                .map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        Map<Long, Boolean> stockMap = null;
        try {
            R r = wareFeignService.getSkusHasStock(skuIds);
            TypeReference<List<SkuHasStockTo>> typeReference
                    = new TypeReference<List<SkuHasStockTo>>() {};
            stockMap = r.getData(typeReference).stream()
                    .collect(Collectors.toMap(SkuHasStockTo::getSkuId, item -> item.isHasStock()));
        }catch (Exception e){
            //如果库存服务调用失败，输出异常信息，默认为有库存继续执行
            log.error("库存服务查询异常：原因{}",e);
        }

        //遍历每一个sku,封装上架检索数据,收集成一个集合
        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModelTo> upSkuTos = skus.stream().map(sku -> {
            //创建To实体类组装需要的数据
            SkuEsModelTo esModel = new SkuEsModelTo();
            //将属性名一样的直接copy
            BeanUtils.copyProperties(sku,esModel);
            /**
             * 余下不同的属性单独处理
             * private List<Attrs> attrs;
             * @Data
             * public static class Attrs{
             *
             *    private Long attrId;
             *    private String attrName;
             *    private String attrValue;
             *
             * }
             */
            //skuPrice,skuImg属性名不同手动封装
            esModel.setSkuPrice(sku.getPrice());
            esModel.setSkuImg(sku.getSkuDefaultImg());
            //hasStock,hotScore需要查询处理后封装
            //1、发送远程调用，库存系统查询是否有库存，
            // 避免多次远程调用，循环外一次性查询,这里只封装
            if (finalStockMap == null){
                esModel.setHasStock(true);
            }else {
                esModel.setHasStock(finalStockMap.get(sku.getSkuId()));
            }

            //TODO 2、热度评分。初值为0 或者可以扩展成刚上架热度置顶等
            esModel.setHotScore(0L);

            //3、查询品牌和分类的名称信息封装
            //   (查询可以放在循环外，因为每一个sku的这些属性都是和spu一样的)
            //   brandName，brandImg，catalogName
            esModel.setBrandName(brand.getName());
            esModel.setBrandImg(brand.getLogo());
            esModel.setCatalogName(category.getName());

            //4、查询当前产品所有可检索的规格属性封装（查询放在循环外）
            esModel.setAttrs(attrsList);

            return esModel;
        }).collect(Collectors.toList());

        //TODO 5、数据发送给es进行保存：gulimall-search
        R r = searchFeignService.productStatusUp(upSkuTos);
        if (r.getCode() == 0){
            //全部上架成功，修改当前spu的状态为已上架
            baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        }else {
            //远程调用部分或全部失败
            //TODO 重复调用？接口幂等性；重试机制？
            //feign调用流程
            /**
             * 1、构造请求数据，将对象转化为JSON
             * 2、发送请求进行执行（执行成功会解码响应数据）
             * 3、执行请求会有重试机制
             */
        }

    }


}