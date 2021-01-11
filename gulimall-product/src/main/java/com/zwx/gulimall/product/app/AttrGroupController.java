package com.zwx.gulimall.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.zwx.gulimall.product.entity.AttrEntity;
import com.zwx.gulimall.product.service.AttrAttrgroupRelationService;
import com.zwx.gulimall.product.service.AttrService;
import com.zwx.gulimall.product.service.CategoryService;
import com.zwx.gulimall.product.vo.AttrGroupRelationVo;
import com.zwx.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zwx.gulimall.product.entity.AttrGroupEntity;
import com.zwx.gulimall.product.service.AttrGroupService;
import com.zwx.common.utils.PageUtils;
import com.zwx.common.utils.R;

import javax.annotation.Resource;


/**
 * 属性分组
 *
 * @author zwx
 * @email coderzwx66@163.com
 * @date 2021-01-03 13:35:57
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Resource
    private CategoryService categoryService;

    @Resource
    AttrService attrService;

    @Resource
    AttrAttrgroupRelationService relationService;

    /**
     * /product/attrgroup/{catelogId}/withattr
     * 根据分类id查询分类下所有属性组以及属性组包含的属性
     * @param catelogId 分类id
     * @return 成功信息，以及封装好的结果
     */
    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable Long catelogId){
        //1、查出当前分类下的所有属性分组
        //2、查出每个属性分组下的所有属性
        List<AttrGroupWithAttrsVo> vos = attrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);

        return R.ok().put("data",vos);

    }

    /**
     * 添加属性和属性组关联关系
     * @param vos 封装属性id和属性组id
     * @return 成功信息
     */
//    /product/attrgroup/attr/relation
    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos){
        relationService.saveBatch(vos);
        return R.ok();
    }

    /**
     * 删除分组和属性的关联关系
     * @param vos 封装属性id和属性组id
     * @return 成功信息
     */
//    /product/attrgroup/attr/relation/delete
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] vos){
        attrService.deleteRelation(vos);
        return R.ok();
    }

    /**
     * 查询关联关系——所有属性组下关联的属性
     * @param attrgroupId 属性分组id
     * @return 属性分组关联的属性
     */
//    /product/attrgroup/{attrgroupId}/attr/relation
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId){
        List<AttrEntity> entities = attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data",entities);
    }

    /**
     * 查询关联关系——所有属性分组同分类下没有被关联的属性
     * @param attrgroupId 属性分组id
     * @return 属性组可关联的属性
     */
//    /product/attrgroup/{attrgroupId}/noattr/relation
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId,
                          @RequestParam Map<String, Object> params){
        PageUtils page = attrService.getNoRelationAttr(params,attrgroupId);
        return R.ok().put("page",page);
    }


    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId){
//        PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params,catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        Long catelogId = attrGroup.getCatelogId();
        Long[] path = categoryService.findCatelogPath(catelogId);

        attrGroup.setCatelogPath(path);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
