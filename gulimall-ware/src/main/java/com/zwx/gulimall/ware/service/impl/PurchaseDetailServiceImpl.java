package com.zwx.gulimall.ware.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zwx.common.utils.PageUtils;
import com.zwx.common.utils.Query;

import com.zwx.gulimall.ware.dao.PurchaseDetailDao;
import com.zwx.gulimall.ware.entity.PurchaseDetailEntity;
import com.zwx.gulimall.ware.service.PurchaseDetailService;
import org.springframework.util.StringUtils;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    /**
     * 根据动态的检索条件查询采购需求列表
     * @param params 分页参数及查询参数
     * key：查询参数
     * status: 0,状态
     * wareId：1,仓库id
     * @return 结果集
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<PurchaseDetailEntity> queryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        String status = (String) params.get("status");
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(key)){
            queryWrapper.and(w->{
                w.eq("purchase_id",key).or().eq("sku_id",key);
            });
        }
        if (!StringUtils.isEmpty(status)){
            queryWrapper.eq("status",status);
        }
        if (!StringUtils.isEmpty(wareId)){
            queryWrapper.eq("ware_id",wareId);
        }

        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<PurchaseDetailEntity> listDetailByPurchaseId(Long id) {

        List<PurchaseDetailEntity> purchase_id
                = this.list(
                        new QueryWrapper<PurchaseDetailEntity>()
                                .eq("purchase_id", id));

        return purchase_id;
    }

}