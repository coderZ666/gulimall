package com.zwx.gulimall.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.zwx.gulimall.product.entity.ProductAttrValueEntity;
import com.zwx.gulimall.product.service.ProductAttrValueService;
import com.zwx.gulimall.product.vo.AttrResponseVo;
import com.zwx.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zwx.gulimall.product.service.AttrService;
import com.zwx.common.utils.PageUtils;
import com.zwx.common.utils.R;

import javax.annotation.Resource;


/**
 * 商品属性
 *
 * @author zwx
 * @email coderzwx66@163.com
 * @date 2021-01-03 13:35:57
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;
    @Resource
    ProductAttrValueService productAttrValueService;

    /**
     * 查出商品关联的的规格基本属性
     * /product/attr/base/listforspu/{spuId}
     */
    @GetMapping("/base/listforspu/{spuId}")
    public R baseAttrListForSpu(@PathVariable Long spuId){

        List<ProductAttrValueEntity> entities = productAttrValueService.baseAttrListForSpu(spuId);

        return R.ok().put("data",entities);
    }

    //  /product/attr/sale/list/{catelogId}
    @GetMapping("/sale/list/{catelogId}")
    public R baseSaleList(@RequestParam Map<String, Object> params,
                          @PathVariable Long catelogId){
        PageUtils page = attrService.queryBaseAttrPage(params,catelogId,"sale");
        return R.ok().put("page", page);
    }

    //  /product/attr/base/list/{catelogId}
    @GetMapping("/base/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable Long catelogId){
        PageUtils page = attrService.queryBaseAttrPage(params,catelogId,"base");
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		//AttrEntity attr = attrService.getById(attrId);
        AttrResponseVo attr = attrService.getAttrInfo(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存属性，同时保存属性和属性分类关联关系
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

//    /**
//     * 保存
//     */
//    @RequestMapping("/save")
//    //@RequiresPermissions("product:attr:save")
//    public R save(@RequestBody AttrEntity attr){
//		attrService.save(attr);
//
//        return R.ok();
//    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrResponseVo attr){
		attrService.updateAttr(attr);

        return R.ok();
    }

    /**
     * 修改商品关联的基本属性
     */
    @PostMapping("/update/{spuId}")
    //@RequiresPermissions("product:attr:update")
    public R updateSpuAttr(@PathVariable Long spuId,
                           @RequestBody List<ProductAttrValueEntity> entities){
        productAttrValueService.updateSpuAttr(spuId,entities);

        return R.ok();
    }

//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    //@RequiresPermissions("product:attr:update")
//    public R update(@RequestBody AttrEntity attr){
//		attrService.updateById(attr);
//
//        return R.ok();
//    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
