package com.zwx.gulimall.search;

import com.alibaba.fastjson.JSON;
import com.zwx.gulimall.search.config.GulimallElasticSearchConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@SpringBootTest
class GulimallSearchApplicationTests {

    @Resource
    RestHighLevelClient client;

    /**
     * 测试配置好的整合ElasticSearch配置类是否成功
     */
    @Test
    void contextLoads() {
        System.out.println(client);
    }

    /**
     * 测试数据存储到es
     * 同时，如果指定了id，当这个id已存在时是更新操作
     */
    @Test
    void test() throws IOException {
        //创建一个索引请求，设置要操作的索引（相当于要操作的表）
        IndexRequest indexRequest = new IndexRequest("users");
        //设置数据id，如果不设置会自动生成
        indexRequest.id("1");
        /*设置存储数据内容*/
        //方式一，参数两两组成键值对。一般不使用
//        indexRequest.source("userName","zhangsan","age",18,"gender","男");
        /*
            方式二,传入一个Json字符串
            可以创建实体类对象转化一个json字符串
            配合SpringCloud alibaba提供的JSON工具类
         */
        User user = new User("zwx",24,"男");
        String jsonString = JSON.toJSONString(user);
        indexRequest.source(jsonString, XContentType.JSON);//必须设置XContentType.JSON
        //执行上面设置好的请求操作
        IndexResponse index
                = client.index(indexRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        //提取有用的响应数据
        System.out.println(index);
    }

    /**
     * 复杂检索请求的测试
     */
    @Test
    void searchData() throws IOException{
        //创建检索请求，指定索引（相当于数据库表）
        SearchRequest searchRequest = new SearchRequest("newbank");
        //1、指定DSL，构造检索条件 SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //1.1、构造查询检索条件  QueryBuilders
//        searchSourceBuilder.from();
//        searchSourceBuilder.size();
//        searchSourceBuilder.aggregation();
        searchSourceBuilder.query(QueryBuilders.matchQuery("address","mill"));
        //1.2、构造聚合，按年龄的值分布进行聚合
        TermsAggregationBuilder ageAgg
                = AggregationBuilders.terms("ageAgg").field("age").size(10);
        searchSourceBuilder.aggregation(ageAgg);
        //1.3、构造聚合，计算平均薪资
        AvgAggregationBuilder balanceAvg
                = AggregationBuilders.avg("balanceAvg").field("balance");
        searchSourceBuilder.aggregation(balanceAvg);

        searchRequest.source(searchSourceBuilder);

        System.out.println("检索条件："+searchSourceBuilder);

        //2、同步执行构造好的检索请求，拿到响应
        SearchResponse searchResponse
                = client.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);

        //3、检索结果的分析使用
        System.out.println("检索结果："+searchResponse);
        //3.1、获取所有查到的数据
        SearchHits hits = searchResponse.getHits();//外层大hits
        SearchHit[] searchHits = hits.getHits();//真正命中纪录的hits数组
        for (SearchHit searchHit : searchHits) {
            /*
             这里拿到每一个hit可以获取到的数据如下
                "_index" : "bank",
                "_id" : "970",
                "_score" : 5.4032025,
                "_source" : {
                    真正的数据
                }
             */
//            String index = searchHit.getIndex();
//            String id = searchHit.getId();
//            float score = searchHit.getScore();
            String source = searchHit.getSourceAsString();
            Account account = JSON.parseObject(source, Account.class);
            System.out.println("封装实体类："+account);
        }
        //3.2、获取所有聚合分析数据
        Aggregations aggregations = searchResponse.getAggregations();
//        for (Aggregation aggregation : aggregations) {
////            String name = aggregation.getName();
////            System.out.println("当前聚合名称："+name);
////        }
        //获取ageAgg，年龄分组聚合
        Terms ageAggRes = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : ageAggRes.getBuckets()) {
            String ages = bucket.getKeyAsString();
            long count = bucket.getDocCount();
            System.out.println("年龄为："+ages+" 的数量为："+count);
        }
        //获取balanceAvg，平均薪资
        Avg balanceAvgRes = aggregations.get("balanceAvg");
        System.out.println("平均薪资："+balanceAvgRes.getValue());
    }

    /**
     * （1）、方便检索{
     *     skuId:1
     *     spuId:11
     *     skuTitle:华为xx
     *     price:998
     *     saleCount:99
     *     attrs:[
     *          {
     *              {尺寸：5寸}
     *              {CPU：高通945}
     *              {分辨率：全高清}
     *          }
     *     ]
     *  }
     * 冗余：
     *  相同sku，不同spu的attrs属性全是一样的
     *  假如attrs中有20个attr属性，共计2KB，100万个spu才将冗余2GB内存
     *  100万*20=100万*2KB=2000MB≈2GB
     *
     * （2）、
     *      sku索引{
     *          skuId:1
     *          spuId:11
     *          xxxxxx
     *      }
     *
     *      attrs索引{
     *          spuId:11
     *          attrs:[
     *              {尺寸：5寸}
     *              {CPU：高通945}
     *              {分辨率：全高清}
     *          ]
     *      }
     *      这种方式，假如我们搜索小米
     *      可能存着小米的分类有：粮食、手机、电器......
     *      假如有10000个商品在检索中符合小米这个条件
     *      这10000个商品对应了4000个spu
     *      那么这个分步查询，查询4000个spu的所有可能属性
     *      就需要在ElasticSearch中传递一个请求包含一个数组存放着[4000个spuId]
     *      那么在网络传输中，假如一个Long类型的id，8个字节，就存在
     *      4000*8≈32KB的网络信息传输
     *      如果同时存在百万请求，就是32GB的网络传输
     */

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    private static class User{
        private String userName;
        private Integer age;
        private String gender;
    }

    /**
     * 根据Json生成的封装测试查询的bank实体类
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    private static class Account
    {
        private int account_number;

        private int balance;

        private String firstname;

        private String lastname;

        private int age;

        private String gender;

        private String address;

        private String employer;

        private String email;

        private String city;

        private String state;

    }

}
