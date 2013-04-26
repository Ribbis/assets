<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/"%>
<html>
<head>
    <title>登录</title>
</head>
<body>
<table><tr><td align="center" valign="middle">
    <h1 class="form-signin-heading">${fns:getConfig('productName')}</h1>
    <form id="loginForm" class="form-signin" action="${ctx}/login" method="post">
        <%String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);%>
        <div id="messageBox" class="alert alert-error <%=error==null?"hide":""%>"><button data-dismiss="alert" class="close">×</button>登录失败, 请重试.</div>
        <label class="input-label" for="username">登录名</label>
        <input type="text" id="username" name="username" class="input-block-level required" value="${username}">
        <label class="input-label" for="password">密码</label>
        <input type="password" id="password" name="password" class="input-block-level required">
        <input class="btn btn-large btn-primary" type="submit" value="登 录"/>&nbsp;&nbsp;
        <label for="rememberMe" title="下次不需要再登录"><input type="checkbox" id="rememberMe" name="rememberMe"/> 记住我（公共场所慎用）</label>
        <div id="theme" class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="#">${fns:getDictLabel(theme,'theme','')}<b class="caret"></b></a>
            <ul class="dropdown-menu">
                <c:forEach items="${fns:getDictList('theme')}" var="dict"><li><a href="?theme=${dict.value}">${dict.label}</a></li></c:forEach>
            </ul>
            <!--[if lte IE 6]><script type="text/javascript">$('#theme').hide();</script><![endif]-->
        </div>
    </form>
    Copyright &copy; 2012-${fns:getConfig('copyrightYear')} <a href="http://thinkgem.iteye.com" target="_blank">ThinkGem</a> - Powered By <a href="https://github.com/thinkgem/jeesite" target="_blank">JeeSite</a> ${fns:getConfig('version')}
</td></tr></table>

</body>
</html>