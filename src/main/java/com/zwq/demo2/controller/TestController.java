package com.zwq.demo2.controller;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.sun.org.apache.xalan.internal.xsltc.dom.SortingIterator;
import com.zwq.demo2.entity.CityPO;
import com.zwq.demo2.entity.UserPO;
import com.zwq.demo2.service.CityService;
import com.zwq.demo2.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * created by zwq on 2018/8/29
 */
@RestController
public class TestController {
    private UserService userService;
    private CityService cityService;

    public TestController(UserService userService, CityService cityService) {
        this.userService = userService;
        this.cityService = cityService;
    }

    @GetMapping("get")
    public Map<String,Model> get() {
//        UserPO user = new UserPO();
//        user.setId(1);
//        UserPO userPO =user.selectById();
//        CityPO city = new CityPO();
//        city.setCityId(1);
//        CityPO cityPO = city.selectById();
        UserPO userPO = userService.get();
        CityPO cityPO = cityService.get();
        System.out.println(cityPO);
        UserPO userPO1 = userService.get();
        System.out.println(userPO1);
        Map<String, Model> map = new HashMap<>();
        map.put("user", userPO);
        map.put("city", cityPO);
        return map;
    }
}
