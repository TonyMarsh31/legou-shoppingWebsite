package com.lxs.legou.item.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lxs.legou.core.po.BaseEntity;
import lombok.Data;

@Data
@TableName("brand_")
public class Brand extends BaseEntity {

    @TableField("name_")
    private String name;
    @TableField("image_")
    private String image;
    @TableField("letter_")
    private String letter;

    @TableField(exist = false)
    private Long[] categoryIds; //瞬时属性，品牌所属的分类[1,2,3]

}
