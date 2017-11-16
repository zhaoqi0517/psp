package com.zhaoqi.psp.action;

import com.zhaoqi.psp.domain.LoginData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by qi on 17-11-16.
 */
@Controller
public class LoginAction {

	@RequestMapping(value = "/")
     public ModelAndView index() {
        System.out.println("process /");
        return new ModelAndView(input, formName, new LoginData());
    }

    private static final String input = "index";
    private static final String formName = "auth";


}
