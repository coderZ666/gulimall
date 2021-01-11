package com.zwx.gulimall.search.service;

import com.zwx.common.to.es.SkuEsModelTo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author coderZWX
 * @date 2021-01-11 21:12
 */
public interface ProductSaveService {

    boolean productStatusUp(List<SkuEsModelTo> skuEsModels) throws IOException;

}
