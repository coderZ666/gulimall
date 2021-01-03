package com.zwx.gulimall.order.dao;

import com.zwx.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author zwx
 * @email coderzwx66@163.com
 * @date 2021-01-03 15:15:37
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
