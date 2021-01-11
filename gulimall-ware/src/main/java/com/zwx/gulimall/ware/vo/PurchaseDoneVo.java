package com.zwx.gulimall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author coderZWX
 * @date 2021-01-08 20:57
 */
@Data
public class PurchaseDoneVo {

    @NotNull
    private Long id;//采购单id

    private List<PurchaseItemDoneVo> items;

}
