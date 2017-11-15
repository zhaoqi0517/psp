package com.zhaoqi.psp.action;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by qi on 17-11-15.
 */
@Component
public class UploadAction {


    @RequestMapping( value = "/upload", method = RequestMethod.GET)
    public void upload(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("upload ....");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
