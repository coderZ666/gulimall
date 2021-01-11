package com.zwx.gulimall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.zwx.common.to.SkuHasStockTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zwx.gulimall.ware.entity.WareSkuEntity;
import com.zwx.gulimall.ware.service.WareSkuService;
import com.zwx.common.utils.PageUtils;
import com.zwx.common.utils.R;



/**
 * 商品库存
 *
 * @author zwx
 * @email coderzwx66@163.com
 * @date 2021-01-03 15:20:00
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    /**
     * 查询sku是否有库存
     */
    @PostMapping("/hasstock")
    public R getSkusHasStock(@RequestBody List<Long> skuIds){
        List<SkuHasStockTo> vos = wareSkuService.getSkusHasStock(skuIds);


        return R.ok().put("data",vos);
    }

    /**
     * 列表
     * 存在动态查询条件
     * skuId： 1
     * wareId： 2
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:waresku:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }

//    /**
//     * 列表
//     */
//    @RequestMapping("/list")
//    //@RequiresPermissions("ware:waresku:list")
//    public R list(@RequestParam Map<String, Object> params){
//        PageUtils page = wareSkuService.queryPage(params);
//
//        return R.ok().put("page", page);
//    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:waresku:info")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * TODO 保存应该判断是否相同仓库已经有本次要添加库存的商品了
     *  如果有只将库存加进去，不用创建新的库存信息
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:waresku:save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:waresku:update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:waresku:delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
