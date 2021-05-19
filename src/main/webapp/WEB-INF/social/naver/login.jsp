<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.security.SecureRandom" %>
<%@ page import="java.math.BigInteger" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <title>네이버로그인</title>
</head>
<body>
<%
  String clientId= (String) request.getAttribute("clientId");
  String redirectURI=URLEncoder.encode(request.getAttribute("callbackUrl") + "social/login/naver/callback","UTF-8");
  SecureRandom random=new SecureRandom();
  String state=new BigInteger(130, random).toString();
  String apiURL = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
  apiURL += "&client_id=" + clientId;
  apiURL += "&redirect_uri=" + redirectURI;
  apiURL += "&state=" + state;
  session.setAttribute("state", state);
%>
  <a href="<%=apiURL%>"><img height="50" src="http://static.nid.naver.com/oauth/small_g_in.PNG"/></a>
</body>
</html>