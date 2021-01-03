package com.zwx.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
 */
@MapperScan("com.zwx.gulimall.product.dao")
@SpringBootApplication
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
