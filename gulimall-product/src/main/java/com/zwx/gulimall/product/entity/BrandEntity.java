package com.zwx.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.zwx.common.vaild.AddGroup;
import com.zwx.common.vaild.ListValue;
import com.zwx.common.vaild.UpdateGroup;
import com.zwx.common.vaild.UpdateStatusGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;


/**
 * 品牌
 * 
 * @author zwx
 * @email coderzwx66@163.com
 * @date 2021-01-03 12:40:38
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
	@NotNull(message = "修改品牌必须指定id",groups = {UpdateGroup.class,UpdateStatusGroup.class})
	@Null(message = "新增不能指定id",groups = {AddGroup.class})
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名不能是空",groups = {UpdateGroup.class,AddGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotEmpty(groups = {UpdateGroup.class,AddGroup.class})
	@URL(message = "logo必须是一个合法的url地址",groups = {UpdateGroup.class,AddGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(groups = {UpdateGroup.class,AddGroup.class,UpdateStatusGroup.class})
	//自定义校验，只允许数vals组中的值
	@ListValue(vals = {0,1},
			groups = {UpdateGroup.class,AddGroup.class,UpdateStatusGroup.class},
			message = "状态值只允许0或者1")
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotEmpty(groups = {UpdateGroup.class,AddGroup.class})
	@Pattern(regexp = "^[a-zA-Z]$",message = "首字母必须是一个字母",groups = {UpdateGroup.class,AddGroup.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(groups = {UpdateGroup.class,AddGroup.class})
	@Min(value = 0,message = "排序必须是正整数（>=0）",groups = {UpdateGroup.class,AddGroup.class})
	private Integer sort;

}
