package com.lxs.legou.order.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lxs.legou.core.po.BaseEntity;
import lombok.Data;

@Data
@TableName("order_item_")
public class OrderItem extends BaseEntity {

    @TableField("category_id1_")
    private Long categoryId1;//1级分类

    @TableField("category_id2_")
    private Long categoryId2;//2级分类

    @TableField("category_id3_")
    private Long categoryId3;//3级分类

    @TableField("spu_id_")
    private Long spuId;//SPU_ID

    @TableField("sku_id_")
    private Long skuId;//SKU_ID

    @TableField("order_id_")
    private String orderId;//订单ID

    @TableField("name_")
    private String name;//商品名称

    @TableField("price_")
    private Long price;//单价

    @TableField("num_")
    private Integer num;//数量

    @TableField("money_")
    private Long money;//总金额

    @TableField("pay_money_")
    private Long payMoney;//实付金额

    @TableField("image_")
    private String image;//图片地址

    @TableField("weight_")
    private Integer weight;//重量

    @TableField("post_fee_")
    private Integer postFee;//运费

    @TableField("is_return_")
    private String isReturn;//是否退货,0:未退货，1：已退货

}