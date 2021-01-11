package com.zwx.common.constant;

/**
 * 商品各种枚举
 * @author coderZWX
 * @date 2021-01-06 19:56
 */
public class ProductConstant {

    /**
     * 属性类型枚举
     */
    public enum AttrEnum{
        ATTR_TYPE_BASE(1,"基本属性"),ATTR_TYPE_SALE(0,"销售属性");

        private int code;
        private String msg;

        AttrEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    /**
     * 商品状态枚举
     */
    public enum StatusEnum{
        NEW_SPU(0,"新建"),SPU_UP(1,"商品已上架"),SPU_DOWN(2,"商品已下架");

        private int code;
        private String msg;

        StatusEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

}
