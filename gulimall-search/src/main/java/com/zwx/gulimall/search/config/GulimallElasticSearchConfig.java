package com.zwx.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 整合ElasticSearch
 * 1、导入依赖
 * 2、编写配置类，给容器中注入一个RestHighLevelClient
 * 3、参照文档操作ElasticSearch
 * @author coderZWX
 * @date 2021-01-09 20:24
 */
@Configuration
public class GulimallElasticSearchConfig {

    /**
     * 官网建议的配置
     */
    public static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
//       builder.addHeader("Authorization", "Bearer " + TOKEN);
//       builder.setHttpAsyncResponseConsumerFactory(
//               new HttpAsyncResponseConsumerFactory
//                       .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient esRestClient() {
        RestClientBuilder builder = null;
        builder = RestClient.builder(new HttpHost("192.168.0.104", 9200, "http"));
        RestHighLevelClient client = new RestHighLevelClient(builder);
//       RestHighLevelClient client = new RestHighLevelClient(​​​​​​
//               RestClient.builder(
//                       new HttpHost("localhost", 9200, "http"),
//                       new HttpHost("localhost", 9201, "http")));
        return client;
    }

}
