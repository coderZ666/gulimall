package com.zwx.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zwx.common.utils.PageUtils;
import com.zwx.gulimall.ware.entity.PurchaseEntity;
import com.zwx.gulimall.ware.vo.MergeVo;
import com.zwx.gulimall.ware.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author zwx
 * @email coderzwx66@163.com
 * @date 2021-01-03 15:20:00
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryUnReceivePage(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);

    void received(List<Long> ids);

    void done(PurchaseDoneVo doneVo);
}

