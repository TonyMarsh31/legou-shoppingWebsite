package com.lxs.legou.item.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lxs.legou.core.po.BaseEntity;
import lombok.Data;

@Data
@TableName("spu_detail_")
public class SpuDetail  extends BaseEntity {

    /**
     * 实体编号（唯一标识）
     */
    @TableId(value = "id_", type = IdType.INPUT)
    protected Long id;
    @TableField("description_")
    private String description;// 商品描述
    @TableField("special_spec_")
    private String specialSpec;// 商品特殊规格的名称及可选值模板
    @TableField("generic_spec_")
    private String genericSpec;// 商品的全局规格属性
    @TableField("packing_list_")
    private String packingList;// 包装清单
    @TableField("after_service_")
    private String afterService;// 售后服务


}