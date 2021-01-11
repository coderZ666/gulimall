package com.zwx.gulimall.product.app;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.zwx.common.vaild.AddGroup;
import com.zwx.common.vaild.UpdateGroup;
import com.zwx.common.vaild.UpdateStatusGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zwx.gulimall.product.entity.BrandEntity;
import com.zwx.gulimall.product.service.BrandService;
import com.zwx.common.utils.PageUtils;
import com.zwx.common.utils.R;


/**
 * 品牌
 *
 * @author zwx
 * @email coderzwx66@163.com
 * @date 2021-01-03 13:35:57
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:brand:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    //@RequiresPermissions("product:brand:info")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     * @Valid 表示跟在后面这个参数Bean需要校验
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:brand:save")
    public R save(@Validated(AddGroup.class) @RequestBody BrandEntity brand){
        brandService.save(brand);

        return R.ok();
    }
//    /**
//     * 保存
//     * @Valid 表示跟在后面这个参数Bean需要校验
//     * @param result 必须紧跟校验参数，获取校验信息
//     */
//    @RequestMapping("/save")
//    //@RequiresPermissions("product:brand:save")
//    public R save(@Valid @RequestBody BrandEntity brand, BindingResult result){
//		if (result.hasErrors()){
//		    Map<String,String> map = new HashMap<>();
//		    //1、获取校验错误结果
//            result.getFieldErrors().forEach((item)->{
//                //FieldError 获取到错误提示
//                String message = item.getDefaultMessage();
//                //获取错误的属性名字
//                String field = item.getField();
//                map.put(field,message);
//            });
//            return R.error(400,"提交的数据不合法").put("data",map);
//        }else {
//            brandService.save(brand);
//        }
//        return R.ok();
//    }

    /**
     * 修改增强（修改的同时，视情况修改其他有关联表）
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:brand:update")
    public R update(@Validated(UpdateGroup.class) @RequestBody BrandEntity brand){
		brandService.updateDetail(brand);

        return R.ok();
    }

//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    //@RequiresPermissions("product:brand:update")
//    public R update(@Validated(UpdateGroup.class) @RequestBody BrandEntity brand){
//		brandService.updateById(brand);
//
//        return R.ok();
//    }

    /**
     * 修改显示状态 0,1
     */
    @RequestMapping("/update/status")
    //@RequiresPermissions("product:brand:update")
    public R updateStatus(@Validated(UpdateStatusGroup.class) @RequestBody BrandEntity brand){
		brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:brand:delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
