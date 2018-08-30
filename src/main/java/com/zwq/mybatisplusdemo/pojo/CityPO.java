package com.zwq.mybatisplusdemo.pojo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * created by zwq on 2018/8/29
 */
@Data
public class CityPO  {
    private int cityId;
    private String cityName;
}
