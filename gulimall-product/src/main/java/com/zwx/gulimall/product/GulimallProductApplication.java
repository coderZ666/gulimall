package com.zwx.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 1、整合Mybatis-Plus
 *  （1）导入依赖
 *   <dependency>
 *       <groupId>com.baomidou</groupId>
 *       <artifactId>mybatis-plus-boot-starter</artifactId>
 *       <version>3.2.0</version>
 *   </dependency>
 *  （2）配置
 *      a.配置数据源
 *          1）导入mysql驱动
 *          <dependency>
 *             <groupId>mysql</groupId>
 *             <artifactId>mysql-connector-java</artifactId>
 *             <version>8.0.17</version>
 *         </dependency>
 *         2）在application.yml中配置数据源相关信息
 *      b.配置Mybatis-Plus
 *          1）使用@MapperScan
 *          2）告诉Mybatis-Plus，sql映射文件位置
 * 2、逻辑删除
 *  （1）配置全局的逻辑删除规则（可以省略）
 *  （2）低版本需要配置逻辑删除的组件（高版本省略）
 *  （3）Bean加上逻辑删除注解@TableLogic
 *
 * 3、JSR303
 *  （1）给实体类Bean添加校验注解:javax.validation.constraints
 *      可以定义自己的message提示
 *      此时还并不会什么都校验，需要在需要校验的实体类参数之前加注解
 *  （2）在Controller传入参数前加注解@Valid，表示这个参数需要校验
 */
@EnableFeignClients(basePackages = "com.zwx.gulimall.product.feign")
@MapperScan("com.zwx.gulimall.product.dao")
@EnableDiscoveryClient //开启服务注册与发现
@SpringBootApplication
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
