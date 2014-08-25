<%@tag description="页面模板" pageEncoding="UTF-8"%>
<%@attribute name="title"%>
<%@attribute name="header" fragment="true"%>
<%@attribute name="footer" fragment="true"%>
<%@attribute name="script" fragment="true"%>
<%@attribute name="style" fragment="true"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${title}</title>
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="jslib/dhtmlx/dhtmlx.css"/>
<jsp:invoke fragment="style"></jsp:invoke>
</head>
<body>
  <jsp:invoke fragment="header" />
  <jsp:doBody />
  <jsp:invoke fragment="footer" />
</body>
<script src="jslib/dojo/dojo.js"></script>
<script src="jslib/jquery/jquery-latest.js"></script>
<script src="jslib/dhtmlx/dhtmlx.js"></script>
<jsp:invoke fragment="script" />
</html>
