package com.zhaoqi.psp.action;

import com.zhaoqi.psp.domain.LoginData;
import com.zhaoqi.psp.domain.User;
import com.zhaoqi.psp.util.WebUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

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
    @Input(input)
    @RequestMapping(value = "login*")
    public String submit(HttpServletRequest request,
                         @ModelAttribute(value = formName)   final LoginData data) {

        User user = userService.login(data);
        WebUtil.setUser(user);
        return "/";
    }

    @RequestMapping(value = "download*")
    public ModelAndView download() {

//        ModelAndView mav = new ExcelModel("入庫通知");
//        mav.addObject("user", new User(1, "a"));
//        List<User> users = Arrays.asList(new User[]{new User(2, "あ"), new User(3, "い")});
//        mav.addObject("users", users);

//        return mav;
        return null;
    }


    private static final String input = "index";
    private static final String formName = "auth";


}
