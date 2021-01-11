package com.zwx.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author coderZWX
 * @date 2021-01-07 20:50
 */
@Data
public class SkuReductionTo {

    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;

}
