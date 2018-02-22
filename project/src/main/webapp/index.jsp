<%@ page language="java" pageEncoding="utf-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
<h2>Hello MUMIN!!~!!!!</h2>
<form action="login/50f81924ba7c15f8846e352031e91905" method="post">
登录名:<input type="text" name="loginName"></input><br />
密码:<input type="text" name="loginPass"></input><br />
域名称:<input type="text" name="domainName"></input><br />
authType:<input type="text" name="authType"></input><br />
<input type="submit" name="submit" value="登录"></input>
</form>


<form action="registe" method="post">
登录名:<input type="text" name="loginName"></input><br />
密码:<input type="text" name="loginPass"></input><br />
姓名:<input type="text" name="userName"></input><br />
手机号:<input type="text" name="userPhone"></input><br />
邮箱:<input type="text" name="userEmail"></input><br />
域名称:<input type="text" name="domainName"></input><br />
<input type="submit" name="submit" value="注册"></input>
</form>

<form action="bindUserRelation/dec57450d88df320762aa9e28e49fcae" method="post">
mainAccessToken:<input type="text" name="mainAccessToken"></input><br />
domainName:<input type="text" name="domainName"></input><br />
loginName:<input type="text" name="loginName"></input><br />
loginPass:<input type="text" name="loginPass"></input><br />
authType:<select name="authType">
			<option value="AuthUserNameAndPwd">验证用户名密码</option>
			<option value="AuthEmail">验证邮箱</option>
			<option value="AuthMobileNo">验证手机号</option>
			<option value="NoAuth">无验证</option>
		</select><br />
<input type="submit" name="submit" value="绑定用户关系"></input>
</form>

<form action="ssoLogin/dec57450d88df320762aa9e28e49fcae" method="post">
mainAccessToken:<input type="text" name="mainAccessToken"></input><br />
domainName:<input type="text" name="domainName"></input><br />
authType:<select name="authType">
			<option value="AuthUserNameAndPwd">验证用户名密码</option>
			<option value="AuthEmail">验证邮箱</option>
			<option value="AuthMobileNo">验证手机号</option>
			<option value="NoAuth">无验证</option>
		</select><br />
<input type="submit" name="submit" value="子应用登陆"></input>
</form>

<form action="logout/7d8cd789a4b98e704d5dba83e9ab585c1cb69406" method="post">
accessToken:<input type="text" name="accessToken"></input><br />
<input type="submit" name="submit" value="注销"></input>
</form>

<form action="validateCode/7d8cd789a4b98e704d5dba83e9ab585c1cb69406" method="post">
accessToken:<input type="text" name="accessToken"></input><br />
validateCode:<input type="text" name="validateCode"></input><br />
codeType:<input type="text" name="codeType"></input><br />
<input type="submit" name="submit" value="校验验证码"></input>
</form>
</body>
</html>
