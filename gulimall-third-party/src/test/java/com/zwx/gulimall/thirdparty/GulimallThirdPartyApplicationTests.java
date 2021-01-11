package com.zwx.gulimall.thirdparty;

import com.aliyun.oss.OSSClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 使用阿里云对象存储的使用
 * 1、引入starter依赖
 * 使用SpringCloud alibaba
 * <dependency>
 *     <groupId>com.alibaba.cloud</groupId>
 *     <artifactId>spring-cloud-starter-alicloud-oss</artifactId>
 * </dependency>
 * 普通使用
 * <dependency>
 *     <groupId>com.aliyun.oss</groupId>
 *     <artifactId>aliyun-sdk-oss</artifactId>
 *     <version>3.10.2</version>
 * </dependency>
 * 2、配置key，endpoint等相关信息
 * 使用SpringCloud alibaba则配置yml
 * spring:
 *   cloud:
 *     alicloud:
 *       access-key: LTAI4Fy2TfCw9NWXXQ9ryJg3
 *       secret-key: qQARVWreNvE9lTXokuXBdZaStiOHaU
 *       oss:
 *         endpoint: oss-cn-beijing.aliyuncs.com
 * 3、使用OSSClient进行相关操作
 */
@SpringBootTest
class GulimallThirdPartyApplicationTests {

    @Resource
    OSSClient ossClient;

    @Test
    void testUpload() throws FileNotFoundException {

//        // Endpoint以杭州为例，其它Region请按实际情况填写。
//        String endpoint = "oss-cn-beijing.aliyuncs.com";
//        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
//        String accessKeyId = "LTAI4Fy2TfCw9NWXXQ9ryJg3";
//        String accessKeySecret = "qQARVWreNvE9lTXokuXBdZaStiOHaU";
//
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件流。
        InputStream inputStream = new FileInputStream("D:\\桌面文件\\EX3UW25X~ULQH$O(TRG)FX4.png");
        ossClient.putObject("zwx-gulimall", "test.jpg", inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

}
