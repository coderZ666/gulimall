package com.zwx.gulimall.ware.vo;

import lombok.Data;

/**
 * @author coderZWX
 * @date 2021-01-08 20:58
 */
@Data
public class PurchaseItemDoneVo {
    //{itemId:1,status:4,reason:""}
    private Long itemId;
    private Integer status;
    private String reason;
}

