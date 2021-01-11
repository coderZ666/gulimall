package com.zwx.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.zwx.common.to.es.SkuEsModelTo;
import com.zwx.gulimall.search.config.GulimallElasticSearchConfig;
import com.zwx.gulimall.search.constant.EsConstant;
import com.zwx.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author coderZWX
 * @date 2021-01-11 21:14
 */
@Slf4j
@Service
public class ProductSaveServiceImp implements ProductSaveService {

    @Resource
    RestHighLevelClient restHighLevelClient;

    @Override
    public boolean productStatusUp(List<SkuEsModelTo> skuEsModels) throws IOException {
        //保存到es
        //1、给es中建立索引。product，建立好映射关系
        // 建索引的指令数据在resources下的product-mapping.txt文件中

        //2、给es中保存这些数据
        //BulkRequest bulkRequest , RequestOptions options
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModelTo model : skuEsModels) {
            //1、构造保存请求
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(model.getSkuId().toString());
            String s = JSON.toJSONString(model);
            indexRequest.source(s, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = restHighLevelClient
                .bulk(bulkRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);

        //TODO 1、如果批量向保存数据出现错误，某商品上架失败，还需要处理错误信息
        boolean b = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        log.info("商品上架成功：{}",collect);

        //如果有错返回false，没错返回true
        return !b;

    }
}
