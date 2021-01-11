package com.zwx.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author coderZWX
 * @date 2021-01-07 19:16
 */
@Data
public class SpuBoundTo {

    private Long spuId;

    private BigDecimal buyBounds;

    private BigDecimal growBounds;

}
