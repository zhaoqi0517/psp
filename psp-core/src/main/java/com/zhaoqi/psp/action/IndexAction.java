package com.zhaoqi.psp.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by qi on 17-11-16.
 */
@Controller
public class IndexAction {

    @RequestMapping()
    public String index() {
        System.out.println("process index.");
        return "/entry/index";
    }

}
