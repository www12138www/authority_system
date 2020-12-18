package com.hopu.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类，定义通用属性，可以不继承
 */

//它在表里的很多其他属性都没写，是因为其他的类可以继承它，它就是为了给其他类继承的
public class BaseEntity implements Serializable {

	private String id;

	private Date createTime;

	private Date updateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
