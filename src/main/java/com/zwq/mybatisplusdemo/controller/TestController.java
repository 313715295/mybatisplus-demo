package com.zwq.mybatisplusdemo.controller;

import com.zwq.mybatisplusdemo.pojo.CityPO;
import com.zwq.mybatisplusdemo.pojo.UserPO;
import com.zwq.mybatisplusdemo.service.CityService;
import com.zwq.mybatisplusdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public Map<UserPO,CityPO> get() {
        UserPO userPO = userService.get();
        CityPO cityPO = cityService.get();
        Map<UserPO, CityPO> map = new HashMap<>();
        map.put(userPO, cityPO);
        return map;
    }
}
