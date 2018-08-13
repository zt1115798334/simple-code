package com.zt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/11 0:20
 * description:
 */
@Controller
public class PageController {

    @RequestMapping("index")
    public String index(){
        System.out.println("Page.index");
        return "index";
    }
}
