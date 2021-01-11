package com.zwx.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zwx.common.constant.ProductConstant;
import com.zwx.common.utils.PageUtils;
import com.zwx.common.utils.Query;
import com.zwx.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.zwx.gulimall.product.dao.AttrGroupDao;
import com.zwx.gulimall.product.dao.CategoryDao;
import com.zwx.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.zwx.gulimall.product.entity.AttrGroupEntity;
import com.zwx.gulimall.product.entity.CategoryEntity;
import com.zwx.gulimall.product.service.CategoryService;
import com.zwx.gulimall.product.vo.AttrGroupRelationVo;
import com.zwx.gulimall.product.vo.AttrResponseVo;
import com.zwx.gulimall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zwx.gulimall.product.dao.AttrDao;
import com.zwx.gulimall.product.entity.AttrEntity;
import com.zwx.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Resource
    AttrAttrgroupRelationDao relationDao;

    @Resource
    AttrGroupDao attrGroupDao;

    @Resource
    CategoryDao categoryDao;

    @Resource
    CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {
        /*1、首先保存基本数据AttrEntity到属性表*/
        //创建表属性
        AttrEntity attrEntity = new AttrEntity();
        //将vo属性赋值给表属性实体
        BeanUtils.copyProperties(attr,attrEntity);
        //存入数据库
        this.save(attrEntity);
        /*2、然后保存属性和属性组的n*n关联关系*/
        if (attr.getAttrType()== ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()
                && attr.getAttrGroupId()!=null){
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            relationDao.insert(relationEntity);
        }
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type) {
        //构造查询
        QueryWrapper<AttrEntity> queryWrapper =
                new QueryWrapper<AttrEntity>()
                        .eq("attr_type","base".equalsIgnoreCase(type)?ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode():ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        //如果分类id不是零，表示查询对应分类id下的属性,拼接查询条件.否则表示查询全部，不做额外操作
        if (catelogId != 0){
            queryWrapper.eq("catelog_id",catelogId);
        }
        //再获取key判断是否有用户输入的查询参数
        String key = (String) params.get("key");
        //如果有查询参数拼接模糊查询
        if (!StringUtils.isEmpty(key)){
            queryWrapper.eq("attr_id",key).or().like("attr_name",key);
        }
        //进行分页查询
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrResponseVo> responseVos = records.stream().map((attrEntity) -> {
            AttrResponseVo attrResponseVo = new AttrResponseVo();
            BeanUtils.copyProperties(attrEntity, attrResponseVo);
            if ("base".equalsIgnoreCase(type)){
                //1、设置分类和分组名字
                //根据属性id在分类属性关联表中查出分类
                AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(
                        new QueryWrapper<AttrAttrgroupRelationEntity>()
                                .eq("attr_id", attrEntity.getAttrId()));
                if (relationEntity != null && relationEntity.getAttrGroupId()!=null) {
                    //获取到所属分类id
                    Long attrGroupId = relationEntity.getAttrGroupId();
                    //根据分类id查询分类名封装进返回结果
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
                    attrResponseVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }

            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrResponseVo.setCatelogName(categoryEntity.getName());
            }

            return attrResponseVo;
        }).collect(Collectors.toList());
        pageUtils.setList(responseVos);
        return pageUtils;
    }

    @Override
    public AttrResponseVo getAttrInfo(Long attrId) {
        AttrResponseVo attrResponseVo = new AttrResponseVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity,attrResponseVo);

        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            //1、设置分组信息
            AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(
                    new QueryWrapper<AttrAttrgroupRelationEntity>()
                            .eq("attr_id", attrId));
            if (relationEntity!=null){
                attrResponseVo.setAttrGroupId(relationEntity.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                if (attrGroupEntity!=null){
                    attrResponseVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }

        //2、设置分类信息
        Long catelogId = attrEntity.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        attrResponseVo.setCatelogPath(catelogPath);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if (categoryEntity!=null){
            attrResponseVo.setCatelogName(categoryEntity.getName());
        }

        return attrResponseVo;
    }

    @Override
    public void updateAttr(AttrResponseVo attr) {
        //基本修改
        AttrEntity attrEntity =new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        this.updateById(attrEntity);

        Integer count = relationDao.selectCount(
                new QueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq("attr_id", attr.getAttrId()));
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            //1、修改/新增分组关联
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attr.getAttrId());
            if (count>0){//表示修改
                relationDao.update(
                        relationEntity,
                        new UpdateWrapper<AttrAttrgroupRelationEntity>()
                                .eq("attr_id",attr.getAttrId()));

            }else {//表示新增
                relationDao.insert(relationEntity);
            }
        }
    }

    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> entities
                = relationDao.selectList(
                        new QueryWrapper<AttrAttrgroupRelationEntity>()
                                .eq("attr_group_id", attrgroupId));
        List<Long> attrIds = entities.stream().map((attr) -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        if (attrIds == null || attrIds.size() == 0){
            return null;
        }
        Collection<AttrEntity> attrEntities = this.listByIds(attrIds);
        return (List<AttrEntity>) attrEntities;
    }

    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {
        /*relationDao.delete(
                new QueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq("attr_id",1)
                        .eq("attr_group_id",1));*/
        List<AttrAttrgroupRelationEntity> entities = Arrays.asList(vos).stream().map((item) -> {
            AttrAttrgroupRelationEntity relationEntity
                    = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        relationDao.deleteBatchRelation(entities);

    }

    /**
     * 获取当前分组没有关联的所有属性
     * @param params 分页参数
     * @param attrgroupId 分组id
     * @return 没有关联的属性
     */
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        //1、当前分组只能关联自己所属分类里的所有属性
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        //2、当前分组只能关联别的分组没有引用的属性
        //  1)查出当前分类下的所有分组
        List<AttrGroupEntity> group
                = attrGroupDao.selectList(
                        new QueryWrapper<AttrGroupEntity>()
                                .eq("catelog_id", catelogId));
        //  2)找到这些分组关联的属性
        List<Long> groupIds = group.stream().map((item) -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());
        List<AttrAttrgroupRelationEntity> relationEntities
                = relationDao.selectList(
                        new QueryWrapper<AttrAttrgroupRelationEntity>()
                                .in("attr_group_id", groupIds));
        List<Long> attrIds = relationEntities.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());
        //  3)从当前分类的所有属性中除去这些属性剩下的就是没人关联的属性
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>()
                .eq("catelog_id", catelogId)
                .eq("attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if (attrIds!=null && attrIds.size()>0){
            wrapper.notIn("attr_id", attrIds);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            wrapper.and((w)->{
                w.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params), wrapper);
        PageUtils pageUtils = new PageUtils(page);
        return pageUtils;
    }

    @Override
    public List<Long> selectSearchAttrIds(List<Long> attrIds) {
        /*
          select attr_id from `pms_attr` where attr_id in(?) and search_type = 1
        */
        return baseMapper.selectSearchAttrIds(attrIds);
    }

}