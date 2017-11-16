package com.zhaoqi.psp.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qi on 17-11-15.
 */
@Controller
public class UploadAction {


    @RequestMapping( value = "/upload", method = RequestMethod.GET)
    public @ResponseBody Map upload(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("upload ....");
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, String> rs = new HashMap<String, String>();
        rs.put("success", "true");
        rs.put("status", "ok");
        return rs;
    }

}
