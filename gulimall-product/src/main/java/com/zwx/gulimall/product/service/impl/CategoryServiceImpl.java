package com.zwx.gulimall.product.service.impl;

import com.zwx.common.utils.PageUtils;
import com.zwx.common.utils.Query;
import com.zwx.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zwx.gulimall.product.dao.CategoryDao;
import com.zwx.gulimall.product.entity.CategoryEntity;
import com.zwx.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Transactional
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

//    @Resource
//    CategoryDao categoryDao;
    @Resource
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //1、查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        //2、组装成父子的树形结构
        //2.1)、找到所有一级分类（父分类id=0）
        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0
        ).map((menu)->{
            menu.setChildren(getChildren(menu,entities));
            return menu;
        }).sorted((menu1,menu2)->{
            return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());
        }).collect(Collectors.toList());
        return level1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 1、检查当前删除的菜单，是否被别的地方引用

        //逻辑删除
        baseMapper.deleteBatchIds(asList);
        //物理删除
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> path = new ArrayList<>();
        findParentPath(catelogId, path);
        Collections.reverse(path);
        System.out.println(path);
        return path.toArray(new Long[0]);
    }

    @Override
    public void updateCascade(CategoryEntity category) {
        //首先更新自己表的信息
        this.updateById(category);
        /*同步更新其他关联表的数据*/
        //1、更新品牌分类关联表中关联的品牌名称
        if (!StringUtils.isEmpty(category.getName())){
            categoryBrandRelationService
                    .updateCategoryName(category.getCatId(),category.getName());
        }
        //TODO 更新其他关联
    }

    /**
     * 根据孙id递归查到[父id,子id,孙id]的完整id数组
     * @param catelogId 孙id
     * @param path id数组
     */
    private void findParentPath(Long catelogId,List<Long> path){
        //1、收集当前节点id
        path.add(catelogId);
        //2、查出父节点
        CategoryEntity parent = this.getById(catelogId);
        //3、如果查到的父节点不是一级节点，继续执行一次方法
        if (parent.getParentCid()!=0){
            findParentPath(parent.getParentCid(),path);
        }
    }

    /**
     * 递归查找所有菜单和子菜单
     * @param root 当前菜单
     * @param all 所有菜单
     * @return 当前菜的的子菜单集合
     */
    private List<CategoryEntity> getChildren(CategoryEntity root,List<CategoryEntity> all){
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid().equals(root.getCatId());
        }).map(categoryEntity -> {
            //找到子菜单（再继续递归找子菜单的子菜单）
            categoryEntity.setChildren(getChildren(categoryEntity,all));
            return categoryEntity;
        }).sorted((menu1,menu2)->{
            //菜单的排序
            return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());
        }).collect(Collectors.toList());
        return children;
    }

}