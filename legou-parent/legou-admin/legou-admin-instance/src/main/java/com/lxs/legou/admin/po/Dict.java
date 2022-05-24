package com.lxs.legou.admin.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lxs.legou.core.po.BaseEntity;
import lombok.Data;

@Data
@TableName("dict_")
public class Dict extends BaseEntity {

	@TableField("value_")
	private String value;	// 数据值
	@TableField("label_")
	private String label;	// 标签名
	@TableField("type_")
	private String type;	// 类型
	@TableField("description_")
	private String description;// 描述

}
