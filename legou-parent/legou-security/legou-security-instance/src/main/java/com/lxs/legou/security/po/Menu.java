package com.lxs.legou.security.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lxs.legou.core.po.BaseTreeEntity;
import lombok.Data;

/**
 * @file Menu.java
 * @Copyright (C) http://www.lxs.com
 * @author lxs
 * @email lxosng77@163.com
 * @date 2018/7/13
 */
@Data
@TableName("menu_")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Menu extends BaseTreeEntity {

	@TableField("path_")
	private String path;
	@TableField("name_")
	private String name;
	@TableField("component_")
	private String component;
	@TableField("hide_in_menu_")
	private Boolean hideInMenu = false; //设为true后在左侧菜单不会显示该页面选项
	@TableField("not_cache_")
	private Boolean notCache = false; //设为true后页面不会缓存
	@TableField("icon_")
	private String icon; //该页面在左侧菜单、面包屑和标签导航处显示的图标，如果是自定义图标，需要在图标名称前加下划线'_'

	@TableField(exist = false)
	private Long roleId; //查询条件，拥有查询角色的菜单
	@TableField(exist = false)
	private Long userId; //查询条件，拥有查询用户的菜单
	@TableField(exist = false)
	private boolean selected; //角色选择菜单，选中角色已有的菜单

}
