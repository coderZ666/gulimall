package com.zwx.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zwx.common.utils.PageUtils;
import com.zwx.common.utils.Query;
import com.zwx.gulimall.product.dao.BrandDao;
import com.zwx.gulimall.product.dao.CategoryDao;
import com.zwx.gulimall.product.entity.BrandEntity;
import com.zwx.gulimall.product.entity.CategoryEntity;
import com.zwx.gulimall.product.service.BrandService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zwx.gulimall.product.dao.CategoryBrandRelationDao;
import com.zwx.gulimall.product.entity.CategoryBrandRelationEntity;
import com.zwx.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Resource
    BrandDao brandDao;
    @Resource
    CategoryDao categoryDao;
    @Resource
    CategoryBrandRelationDao relationDao;
    @Resource
    BrandService brandService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        //1、查询详细名字
        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);

        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());

        this.save(categoryBrandRelation);
    }

    @Override
    public void updateBrandName(Long brandId, String name) {
        CategoryBrandRelationEntity relationEntity = new CategoryBrandRelationEntity();
        relationEntity.setBrandId(brandId);
        relationEntity.setBrandName(name);
        this.update(relationEntity,
                new UpdateWrapper<CategoryBrandRelationEntity>()
                        .eq("brand_id",brandId));
    }

    @Override
    public void updateCategoryName(Long catId, String name) {
        this.baseMapper.updateCategory(catId,name);
    }

    @Override
    public List<BrandEntity> getBrandsByCatId(Long catId) {
        List<CategoryBrandRelationEntity> catelogId
                = relationDao.selectList(
                        new QueryWrapper<CategoryBrandRelationEntity>()
                                .eq("catelog_id", catId));
        List<BrandEntity> collect = catelogId.stream().map((item) -> {
            Long brandId = item.getBrandId();
            BrandEntity byId = brandService.getById(brandId);
            return byId;
        }).collect(Collectors.toList());
        return collect;
    }

}