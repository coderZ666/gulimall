package com.zwx.gulimall.coupon.service.impl;

import com.zwx.common.to.MemberPrice;
import com.zwx.common.to.SkuReductionTo;
import com.zwx.gulimall.coupon.entity.MemberPriceEntity;
import com.zwx.gulimall.coupon.entity.SkuLadderEntity;
import com.zwx.gulimall.coupon.service.MemberPriceService;
import com.zwx.gulimall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zwx.common.utils.PageUtils;
import com.zwx.common.utils.Query;

import com.zwx.gulimall.coupon.dao.SkuFullReductionDao;
import com.zwx.gulimall.coupon.entity.SkuFullReductionEntity;
import com.zwx.gulimall.coupon.service.SkuFullReductionService;

import javax.annotation.Resource;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Resource
    SkuLadderService skuLadderService;
    @Resource
    MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTo reductionTo) {
        // gulimall_sms->sms_sku_ladder/sms_sku_full_reduction/sms_member_price/sms_spu_bounds
        //1、保存满减打折、会员价
        //sms_sku_ladder 打折
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(reductionTo.getSkuId());
        skuLadderEntity.setFullCount(reductionTo.getFullCount());
        skuLadderEntity.setDiscount(reductionTo.getDiscount());
        skuLadderEntity.setAddOther(reductionTo.getCountStatus());
        //如果存在正确的打折信息则保存
        if (reductionTo.getFullCount()>0){
            skuLadderService.save(skuLadderEntity);
        }

        //2、sms_sku_full_reduction 满减
        SkuFullReductionEntity reductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(reductionTo,reductionEntity);
        //如果存在正确的满减信息则保存
        if (reductionEntity.getFullPrice().compareTo(new BigDecimal("0"))==1){
            this.save(reductionEntity);
        }

        //3、sms_member_price 会员价
        List<MemberPrice> memberPrice = reductionTo.getMemberPrice();

        List<MemberPriceEntity> collect = memberPrice.stream().map(item -> {
            MemberPriceEntity priceEntity = new MemberPriceEntity();
            priceEntity.setSkuId(reductionTo.getSkuId());
            priceEntity.setMemberLevelId(item.getId());
            priceEntity.setMemberLevelName(item.getName());
            priceEntity.setMemberPrice(item.getPrice());
            priceEntity.setAddOther(1);
            return priceEntity;
        }).filter(item->{
            return item.getMemberPrice().compareTo(new BigDecimal("0"))==1;
        }).collect(Collectors.toList());
        memberPriceService.saveBatch(collect);
    }

}