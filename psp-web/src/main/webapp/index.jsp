<%@ page import="com.zhaoqi.psp.services.UserService" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<html>
<body>
<h2>
    <%
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
        UserService user = (UserService)wac.getBean("userService");
        user.setName("MR.Zhao");
        %>
    <%=user.getName()%></h2>
</body>
</html>
