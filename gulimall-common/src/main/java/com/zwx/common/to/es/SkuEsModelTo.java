package com.zwx.common.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * PUT product
 * {
 *   "mappings":{
 *     "properties":{
 *       //sku商品id，用于点击可以查到商品详情，不用于检索
 *       "skuId":{
 *         "type":"long"
 *       },
 *        //spu商品id，用于明确当前spu属于哪个sku
 *       //未来会有一个数据折叠功能，所以设计为keyword
 *       "spuId":{
 *         "type":"keyword"
 *       },
 *       //商品标题，需要进行全文分词检索所以type是text
 *       //使用扩展的ik分词器
 *       "skuTitle":{
 *         "type":"text",
 *         "analyzer": "ik_smart"
 *       },
 *       //商品价格，需要区间检索或精确检索，不需要分词所以是keyword
 *       "skuPrice":{
 *         "type":"keyword"
 *       },
 *       //商品默认展示图片,希望一次查询就可以展示所有页面需要的信息
 *       //因此保存这个字段，但是设置下面两个false表示不参与检索和聚合
 *       "skuImg":{
 *         "type":"keyword",
 *         "index": false,
 *         "doc_values": false
 *       },
 *       //商品销量，可以按照销量排序的检索
 *       "saleCount":{
 *         "type":"long"
 *       },
 *       //是否有库存不保存数值，只保存boolean表示有无库存
 *       //（例如仅显示有货的检索）
 *       //不保存实际库存量是因为修改字段会重新索引，频繁修改浪费性能
 *       //所以只在没有库存的时候来修改一次
 *       "hasStock":{
 *         "type":"boolean"
 *       },
 *       //商品的热度评分（后来再说）
 *       "hotScore":{
 *         "type":"long"
 *       },
 *       //品牌id，按品牌检索时使用
 *       "brandId":{
 *         "type":"long"
 *       },
 *       //分类id，按分类检索时使用
 *       "catelogId":{
 *         "type":"long"
 *       },
 *       //品牌名，只用来看不检索
 *       //冗余存储，以期一次ES查询直接可以在页面展示所有信息
 *       "brandName":{
 *         "type":"keyword",
 *         "index": false,
 *         "doc_values": false
 *       },
 *       //品牌图片，只用来看不检索
 *       //冗余存储，以期一次ES查询直接可以在页面展示所有信息
 *       "brandImg":{
 *         "type":"keyword",
 *          "index": false,
 *         "doc_values": false
 *       },
 *       //分类名，只用来看不检索
 *       //冗余存储，以期一次ES查询直接可以在页面展示所有信息
 *       "catalogName":{
 *         "type":"keyword",
 *          "index": false,
 *          "doc_values": false
 *       },
 *       //所有当前商品的规格属性参数
 *       //里边保存属性id、属性名和属性值
 *       //这里的nasted很重要
 *       //嵌入式，表示要检索的是attrs数组内部的属性
 *       //如果没有这个类型，数据会被扁平化处理，出现检索的问题
 *       "attrs":{
 *         "type":"nested",
 *         "properties": {
 *           "attrId":{
 *             "type":"long"
 *           },
 *           //属性名只用来展示，不用来检索
 *           "attrName":{
 *             "type":"keyword",
 *             "index":false,
 *             "doc_values":false
 *           },
 *           //属性值需要用来检索
 *           //例如属性名为cpu，值为麒麟980，用麒麟980来精确检索
 *           "attrValue": {
 *             "type":"keyword"
 *           }
 *         }
 *       }
 *     }
 *   }
 * }
 * @author coderZWX
 * @date 2021-01-11 15:32
 */
@Data
public class SkuEsModelTo {


    //sku商品id，用于点击可以查到商品详情
    private Long skuId;

    /**
     * spu商品id，用于明确当前spu属于哪个sku
     * 未来会有一个数据折叠功能，所以设计为keyword
     */
    private Long spuId;

    /**
     * 商品标题，需要进行全文分词检索所以type是text
     * 使用扩展的ik分词器
     */
    private String skuTitle;

    /**
     * 商品价格，需要区间检索或精确检索，不需要分词所以是keyword
     */
    private BigDecimal skuPrice;

    /**
     * 商品默认展示图片,希望一次查询就可以展示所有页面需要的信息
     */
    private String skuImg;

    /**
     * 商品销量，可以按照销量排序的检索
     */
    private Long saleCount;

    /**
     * 是否有库存不保存数值，只保存boolean表示有无库存
     */
    private boolean hasStock;

    /**
     * 商品的热度评分（后来再说）
     */
    private Long hotScore;

    /**
     * 品牌id，按品牌检索时使用
     */
    private Long brandId;

    /**
     * 分类id，按分类检索时使用
     */
    private Long catelogId;

    /**
     * 品牌名，只用来看不检索
     */
    private String brandName;

    /**
     * 品牌图片，只用来看不检索
     */
    private String brandImg;

    /**
     * 分类名，只用来看不检索
     */
    private String catalogName;

    /**
     * spu规格属性集合
     */
    private List<Attrs> attrs;

    /**
     * 静态内部类，表示spu所有规格参数属性
     * 为保证可以对他序列化和反序列化设置为public
     */
    @Data
    public static class Attrs{

        /**
         * 属性id
         */
        private Long attrId;
        /**
         * 属性名
         */
        private String attrName;
        /**
         * 属性值
         */
        private String attrValue;

    }

}
