package com.lxs.legou.security.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lxs.legou.core.po.BaseEntity;
import lombok.Data;

@Data
@TableName("address")
public class Address extends BaseEntity {

    @TableField(value = "username_")
    private String username;//用户名

    @TableField(value = "province_")
    private String provinceid;//省

    @TableField(value = "city_")
    private String cityid;//市

    @TableField(value = "area_")
    private String areaid;// 县/区

    @TableField(value = "phone_")
    private String phone;//手机号

    @TableField(value = "address_")
    private String address;//详细地址

    @TableField(value = "contact_")
    private String contact;//联系人

    @TableField(value = "is_default_")
    private String isDefault;//是否默认地址 1默认 0不默认

    @TableField(value = "alias_")
    private String alias;//别名


}
