package com.zwx.gulimall.ware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.zwx.common.constant.WareConstant;
import com.zwx.gulimall.ware.vo.MergeVo;
import com.zwx.gulimall.ware.vo.PurchaseDoneVo;
import org.springframework.web.bind.annotation.*;

import com.zwx.gulimall.ware.entity.PurchaseEntity;
import com.zwx.gulimall.ware.service.PurchaseService;
import com.zwx.common.utils.PageUtils;
import com.zwx.common.utils.R;

import javax.annotation.Resource;


/**
 * 采购信息
 *
 * @author zwx
 * @email coderzwx66@163.com
 * @date 2021-01-03 15:20:00
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Resource
    private PurchaseService purchaseService;

    /**
     * 采购完成
     * /ware/purchase/done
     */
    @PostMapping("/done")
    public R finish(@RequestBody PurchaseDoneVo doneVo){

        purchaseService.done(doneVo);

        return R.ok();
    }

    /**
     * 领取采购单
     * @param ids 要领取的采购单id集合
     * @return 领取结果信息
     */
    @PostMapping("/received")
    public R received(@RequestBody List<Long> ids){

        purchaseService.received(ids);

        return R.ok();
    }

    /**
     * 合并采购单
     * 如果选择了采购单，则将采购需求集合放入选择的采购单
     * 如果没选择采购单，则将采购需求集合放入一个新建采购单
     * /ware/purchase/merge
     * {
     *   purchaseId: 1, //整单id
     *   items:[1,2,3,4] //合并项集合
     * }
     */
    @PostMapping("/merge")
    public R unReceiveList(@RequestBody MergeVo mergeVo){
        purchaseService.mergePurchase(mergeVo);

        return R.ok();
    }

    /**
     * 查询未领取的采购单
     * /ware/purchase/unreceive/list
     */
    @RequestMapping("/unreceive/list")
    //@RequiresPermissions("ware:purchase:list")
    public R unReceiveList(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryUnReceivePage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){

		purchase.setCreateTime(new Date());
		purchase.setUpdateTime(new Date());
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
