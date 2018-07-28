package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/7/21.
 */
@RestController
public class TestController {

    @RequestMapping("/hello")
    public String index() {
        return "Hello World";
    }

}
