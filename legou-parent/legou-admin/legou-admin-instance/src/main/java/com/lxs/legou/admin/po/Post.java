package com.lxs.legou.admin.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lxs.legou.core.po.BaseEntity;
import lombok.Data;

@Data
@TableName("post_")
public class Post extends BaseEntity {

	@TableField("name_")
	private String name;
	@TableField("title_")
	private String title;
	@TableField("desc_")
	private String desc;

}
