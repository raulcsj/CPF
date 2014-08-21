<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="layout"%>
<layout:tpl>
  <jsp:attribute name="title" trim="true">
  通用平台
  </jsp:attribute>
  <jsp:attribute name="style">
  <style>
  html, body {
   width: 100%;
   height: 100%;
   margin: 0px;
   padding: 0px;
   overflow: hidden;
  }
  
  #my_logo {
   height: 10%;
  }
  
  #my_copy {
   height: 8%
  }
  </style>
  </jsp:attribute>
  <jsp:attribute name="script">
  <script type="text/javascript">
            require([ "dojo/domReady!" ], function() {
                myLayout = new dhtmlXLayoutObject({
                    parent : document.body,
                    pattern : "2U",
                    cells : [ {
                        id : "a",
                        text : "Text",
                        collapsed_text : "Text 2",
                        header : true,
                        width : 200
                    }, {
                        id : "b",
                        header : false
                    } ]
                });
                myLayout.attachHeader("my_logo");
                myLayout.attachFooter("my_copy");
            })
        </script>
  </jsp:attribute>
  <jsp:attribute name="header">
    <div id="my_logo">Hi! I'm header!</div>
  </jsp:attribute>
  <jsp:attribute name="footer">
    <div id="my_copy">Hi! I'm copyright &copy;!</div>
  </jsp:attribute>
  <jsp:body>
  </jsp:body>
</layout:tpl>