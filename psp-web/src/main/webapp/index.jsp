<%@ page import="com.zhaoqi.psp.services.UserService" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page import="com.zhaoqi.psp.domain.User" %>
<html>
<body>
<h2>
    <%
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
        User user = new User();
        user.setName("Zhao");
        user.setRole("Developer");
        user.setId(2);
        UserService userSvc = (UserService)wac.getBean("userService");
        userSvc.save(user);
        %>
    <%=user.getName()%></h2>
</body>
</html>
