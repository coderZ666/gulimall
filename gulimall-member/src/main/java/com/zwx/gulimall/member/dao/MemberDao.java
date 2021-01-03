package com.zwx.gulimall.member.dao;

import com.zwx.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author zwx
 * @email coderzwx66@163.com
 * @date 2021-01-03 15:03:37
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
