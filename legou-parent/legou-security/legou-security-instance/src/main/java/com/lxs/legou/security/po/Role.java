package com.lxs.legou.security.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lxs.legou.core.po.BaseEntity;
import lombok.Data;

/**
 * @Title:
 * @Description:
 *
 * @Copyright 2019 lxs - Powered By 雪松
 * @Author: lxs
 * @Date:  2019/10/9
 * @Version V1.0
 */
@Data
@TableName("role_")
public class Role extends BaseEntity {

	@TableField("name_")
	private String name;
	@TableField("title_")
	private String title;
	@TableField("desc_")
	private String desc;

	@TableField(exist = false)
	private Long[] userIds; //瞬时属性，角色的用户列表，如：[1,3,4,5]

}
