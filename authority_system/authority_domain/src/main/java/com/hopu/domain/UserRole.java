package com.hopu.domain;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 用户角色
 */
//此注解就是声明这个是数据库的哪一个表
@TableName("t_user_role")
public class UserRole implements Serializable {

    // serialVersionUID 的作用是在Java序列化时验证版本一致的。
//    在进行反序列化时，JVM会把传来的字节流中的serialVersionUID于本地相应实体类的serialVersionUID进行比较。
//    如果相同说明是一致的，可以进行反序列化，否则会出现反序列化版本一致的异常，即是InvalidCastException。
//    具体序列化的过程是这样的：序列化操作时会把系统当前类的serialVersionUID写入到序列化文件中，
//    当反序列化时系统会自动检测文件中的serialVersionUID，判断它是否与当前类中的serialVersionUID一致。
//    如果一致说明序列化文件的版本与当前类的版本是一样的，可以反序列化成功，否则就失败；
    private static final long serialVersionUID = 1L;

    private String userId;  // 用户id
    private String roleId; // 角色id


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "UserRole{" +
        "userId=" + userId +
        ", roleId=" + roleId +
        "}";
    }
}
