package com.zwx.gulimall.product.app;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.zwx.gulimall.product.vo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zwx.gulimall.product.entity.SpuInfoEntity;
import com.zwx.gulimall.product.service.SpuInfoService;
import com.zwx.common.utils.PageUtils;
import com.zwx.common.utils.R;



/**
 * spu信息
 *
 * @author zwx
 * @email coderzwx66@163.com
 * @date 2021-01-03 13:35:57
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;

    /**
     * 商品上架，保存到ES中便于检索
     * /product/spuinfo/{spuId}/up
     */
    @PostMapping("/{spuId}/up")
    public R spuUp(@PathVariable("spuId") Long spuId){
        spuInfoService.up(spuId);

        return R.ok();
    }

    /**
     * 列表，带检索
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:spuinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }

//    /**
//     * 列表
//     */
//    @RequestMapping("/list")
//    //@RequiresPermissions("product:spuinfo:list")
//    public R list(@RequestParam Map<String, Object> params){
//        PageUtils page = spuInfoService.queryPage(params);
//
//        return R.ok().put("page", page);
//    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:spuinfo:info")
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存完整商品信息
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:spuinfo:save")
    public R save(@RequestBody SpuSaveVo vo){
//		spuInfoService.save(vo);

        spuInfoService.saveSpuInfo(vo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:spuinfo:update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:spuinfo:delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
