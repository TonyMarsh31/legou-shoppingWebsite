package com.lxs.legou.seckill.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lxs.legou.core.po.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
@TableName("seckill_order_")
public class SeckillOrder extends BaseEntity {

    @TableField("seckill_id_")
    private Long seckillId;//秒杀商品ID

    @TableField("money_")
    private String money;//支付金额

    @TableField("user_id_")
    private String userId;//用户

    @TableField("seller_id_")
    private String sellerId;

    @TableField("create_time_")
    private Date createTime;//创建时间

    @TableField("pay_time_")
    private Date payTime;//支付时间

    @TableField("status_")
    private String status;//状态，0未支付，1已支付

    @TableField("receiver_address_")
    private String receiverAddress;//收货人地址

    @TableField("receiver_mobile_")
    private String receiverMobile;//收货人电话

    @TableField("receiver_")
    private String receiver;//收货人

    @TableField("transaction_id_")
    private String transactionId;//交易流水

}