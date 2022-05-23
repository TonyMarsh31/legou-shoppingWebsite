package com.lxs.legou.security.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lxs.legou.core.po.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("user_")
public class User extends BaseEntity {

	@TableField("user_name_")
	private String userName; //登录名
	@TableField("real_name_")
	private String realName; //真实姓名
	@TableField("password_")
	private String password;
	@TableField("salt")
    private String salt; //加密密码的盐
	@TableField("sex_")
	private String sex;
	@TableField("tel_")
	private String tel;
	@TableField("email_")
	private String email;
	@TableField("desc_")
	private String desc;
	@TableField("lock_")
	private Boolean lock; //是否锁定
	@TableField("birthday_")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date birthday;
	@TableField("principal_")
	private Boolean principal; //是否为部门负责人，用于"常用语:直接上级"
	@TableField("dept_id_")
	private Long deptId; //部门
	@TableField("post_id_")
	private Long postId; //岗位

	@TableField(exist = false)
	private String deptName; //部门名称，用于列表显示

	@TableField(exist = false)
	private Long[] roleIds; //瞬时属性，用户的角色列表，如：[1,3,4,5]

	@TableField(exist = false)
	private String postName; //部门名称，用于列表显示

	public String credentialsSalt() {
		return userName + salt;
	}

}
