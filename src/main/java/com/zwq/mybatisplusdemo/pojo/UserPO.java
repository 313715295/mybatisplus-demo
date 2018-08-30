package com.zwq.mybatisplusdemo.pojo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * created by zwq on 2018/8/29
 */
@Data
@TableName("user")
public class UserPO extends Model<UserPO> {
    private int id;
    private String name;
    @Override
    protected Serializable pkVal() {
        return id;
    }
}
