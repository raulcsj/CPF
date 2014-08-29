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
  </style>
  </jsp:attribute>
  <jsp:attribute name="script">
        <script type="text/javascript">
            var myPop;
            require([ "dojo/domReady!" ], function() {
                myLayout = new dhtmlXLayoutObject({
                    parent : document.body,
                    pattern : "1C",
                    cells : [ {
                        id : "a",
                        header : false
                    }]
                });
                //myLayout.attachHeader("my_logo");
                //myLayout.attachFooter("my_copy");
                /*
                myAcc = myLayout.cells("a").attachAccordion({
                    icons_path: "resources/images/icons/",
                    items: [
                        { id: "authMgr", text: "权限管理", icon: "flag_red.png" },
                        { id: "sysConf", text: "系统设置", icon: "flag_green.png" },
                        { id: "monitor", text: "运行监控", icon: "flag_blue.png" }
                    ]
                });*/
                myRibbon = myLayout.attachRibbon({
                    icons_path: "resources/images/icons/",
                    json: "resources/static/data_attached.json",
                    onload: function(){
                        myPop = new dhtmlXPopup({ ribbon: myRibbon, id: "btn_user_pwd"});
                    }
                });
                myRibbon.attachEvent("onClick", function(id){
                    console.log(id);
                    if(id == "btn_user_update") {
                        var dhxWins = new dhtmlXWindows();
                        var w = screen.availWidth;
                        var h = screen.availHeight;
                        dhxWins.createWindow(id, (w-320)/2, (h-240)/2, 320, 240);
                        dhxWins.window(id).setText("修改密码");
                    } else if(id == "btn_user_pwd") {
                        var myForm = myPop.attachForm([
                            {type: "settings", position: "label-top", labelWidth: 80, inputWidth: 150},
                            {type: "password", label: "原始密码:", name: "_pwd", required:true},
                            {type: "password", label: "新的密码:", name: "pwd", required:true},
                            {type: "password", label: "确认密码:", name: "pwd_", required:true},
                            {type: "button", name: "submit", value: "确定", offsetLeft: 85}
                        ]);
                        myForm.attachEvent("onButtonClick", function(btnId){
                            if(btnId == "submit") {
                                syncDB(ctx + "/submit.html", myForm.getFormData());
                            }
                            myPop.hide();
                        });
                    }
                });
            })
        </script>
  </jsp:attribute>
  <jsp:body>
  </jsp:body>
</layout:tpl>