package com.zwx.gulimall.product.service.impl;

import com.zwx.common.utils.PageUtils;
import com.zwx.common.utils.Query;
import com.zwx.gulimall.product.entity.AttrEntity;
import com.zwx.gulimall.product.service.AttrService;
import com.zwx.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zwx.gulimall.product.dao.AttrGroupDao;
import com.zwx.gulimall.product.entity.AttrGroupEntity;
import com.zwx.gulimall.product.service.AttrGroupService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Transactional
@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Resource
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        /*首先怎么查都需要构造模糊查询*/
        String key = (String)params.get("key");
        //根据查询参数key是否存在，构造动态查询如下
        //SELECT * FROM pms_attr_group WHERE catelog_id = ? AND (attr_group_id = ? OR attr_group_name LIKE %?%)
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();

        if (!StringUtils.isEmpty(key)){
            wrapper.and((obj)->{
                obj.eq("attr_group_id",key).or().like("attr_group_name",key);
            });
        }
        /*然后判断是否有分类id*/
        //如果catelogId为0表示查询所有
        if (catelogId == 0){
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper
            );
            return new PageUtils(page);
        }else { //catelogId不为零表示查询对应catelogId的属性
            wrapper.eq("catelog_id",catelogId);
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper
            );
            return new PageUtils(page);
        }

    }

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        //1、查询分组信息
        List<AttrGroupEntity> attrGroups
                = this.list(
                        new QueryWrapper<AttrGroupEntity>()
                                .eq("catelog_id", catelogId));
        //2、查询所有属性
        List<AttrGroupWithAttrsVo> collect = attrGroups.stream().map(group -> {
            AttrGroupWithAttrsVo vos = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(group,vos);
            List<AttrEntity> attrs = attrService.getRelationAttr(vos.getAttrGroupId());
            vos.setAttrs(attrs);
            return vos;
        }).collect(Collectors.toList());

        return collect;
    }

}