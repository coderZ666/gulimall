package com.zwx.gulimall.product.service.impl;

import com.zwx.common.utils.PageUtils;
import com.zwx.common.utils.Query;
import com.zwx.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zwx.gulimall.product.dao.BrandDao;
import com.zwx.gulimall.product.entity.BrandEntity;
import com.zwx.gulimall.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Transactional
@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Resource
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //获取查询参数key,重新构造查询QueryWrapper
        String key = (String)params.get("key");
        QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(key)){
            queryWrapper.eq("brand_id",key).or().like("name",key);
        }

        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void updateDetail(BrandEntity brand) {
        //跟新自己的信息
        this.updateById(brand);
        /*同步更新其他关联表的数据*/
        //1、更新品牌分类关联表中关联的品牌名称
        if (!StringUtils.isEmpty(brand.getName())){
            categoryBrandRelationService
                    .updateBrandName(brand.getBrandId(),brand.getName());
        }
        //TODO 更新其他关联
    }

}