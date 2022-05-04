package com.lxs.legou.item.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lxs.legou.core.po.BaseTreeEntity;
import lombok.Data;

/**
 * @Title: 商品分类
 * @Description:
 *
 * @Copyright 2019 lxs - Powered By 雪松
 * @Author: lxs
 * @Date:  2019/10/9
 * @Version V1.0
 */
@Data
@TableName("category_")
public class Category extends BaseTreeEntity {

    @TableField("is_parent_")
    private Boolean isParent = false; //是否为父节点

    @TableField(exist = false)
    private Integer isRoot = 0; //值=1 ： 查询根节点条件

    public String getLabel() { //treeselect需要的属性
        return this.getTitle();
    }

}