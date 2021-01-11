package com.zwx.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zwx.gulimall.product.entity.BrandEntity;
import com.zwx.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;


@SpringBootTest
class GulimallProductApplicationTests {

    @Resource
    BrandService brandService;

    @Test
    void contextLoads() {

//        BrandEntity brandEntity =new BrandEntity();
//        brandEntity.setName("华为");
//        brandService.save(brandEntity);
//        System.out.println("保存成功");
//        brandEntity.setBrandId(1L);
//        brandEntity.setDescript("我是测试修改的华为");
//        brandService.updateById(brandEntity);
//        System.out.println("修改成功");
        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        list.forEach((item)->{
            System.out.println(item);
        });

    }

}
