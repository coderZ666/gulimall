package com.zwx.gulimall.product.app;

import java.util.Arrays;
import java.util.List;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zwx.gulimall.product.entity.CategoryEntity;
import com.zwx.gulimall.product.service.CategoryService;
import com.zwx.common.utils.R;



/**
 * 商品三级分类
 *
 * @author zwx
 * @email coderzwx66@163.com
 * @date 2021-01-03 13:35:57
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查出所有分离以及子分类，以树形结构组装起来
     */
    @RequestMapping("/list/tree")
    //@RequiresPermissions("product:category:list")
    public R list(){
        List<CategoryEntity> entities = categoryService.listWithTree();
        return R.ok().put("data", entities);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    //@RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 批量修改,用于拖动顺序后的修改
     */
    @RequestMapping("/update/sort")
    //@RequiresPermissions("product:category:update")
    public R updateSort(@RequestBody CategoryEntity[] category){
		categoryService.updateBatchById(Arrays.asList(category));

        return R.ok();
    }

    /**
     * 修改增强（修改的同时，视情况修改其他有关联表）
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateCascade(category);

        return R.ok();
    }

//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    //@RequiresPermissions("product:category:update")
//    public R update(@RequestBody CategoryEntity category){
//		categoryService.updateById(category);
//
//        return R.ok();
//    }

    /**
     * 删除
     * @RequestBody 获取请求体，必须发送post请求
     * SpringMVC自动将请求体的数据（json），转为对应的对象
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds){
        //1、检查当前删除的菜单，是否被别的地方引用
		//categoryService.removeByIds(Arrays.asList(catIds));
		categoryService.removeMenuByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
