/*
 * 系统通用Function
 * As part of the Request API, providers will emit events during certain operations. 
 * dojo/request/notify is an interface for listening for these events.
 * 1. 监听HTTP所有请求(全局异常等处理)
 * 2. 系统公共方法(打开菜单等..)
 */
var path = window.location.pathname;
var ctx = path.match(/^\/\w+/g);
require([ "dojo/request/notify" ], function(notify) {
    notify("start", function() {
        // Do something when the request queue has started
        // This event won't fire again until "stop" has fired
    });
    notify("send", function(response, cancel) {
        // Do something before a request has been sent
        // Calling cancel() will prevent the request from
        // being sent
    });
    notify("load", function(response) {
        // Do something when a request has succeeded
    });
    notify("error", function(error) {
        // Do something when a request has failed
    });
    notify("done", function(responseOrError) {
        // Do something whether a request has succeeded or failed
        if (responseOrError instanceof Error) {
            /*
             * http://tools.ietf.org/html/rfc2616
             * HTTP Status Code: 
             * - 1xx: Informational - Request received,
             * continuing process
             *  - 2xx: Success - The action was successfully received,
             * understood, and accepted
             *  - 3xx: Redirection - Further action must be taken in order to
             * complete the request
             *  - 4xx: Client Error - The request contains bad syntax or cannot
             * be fulfilled
             *  - 5xx: Server Error - The server failed to fulfill an apparently
             * valid request
             */
            var status = responseOrError.response.status;
            switch (status) {
            case 0:
                alert("网络异常");
                break;
            case 403:
                alert("访问受限");
                break;
            case 404:
                alert("请求资源不存在");
                break;
            case 500:
                alert("服务器内部错误");
                break;
            case 999: /* 自定义异常, session超时处理 */
                alert("会话超时, 请重新登录!");
                top.window.location = ctx + '/login/user';
                break;
            default:
                alert(status);
                break;
            }
            // Do something when a request has failed
        } else {
            // Do something when a request has succeeded
        }
    });
    notify("stop", function() {
        // Do something when all in-flight requests have finished
    });
});
require([ 
         "dojo/_base/array",
         "dojo/_base/declare",
         "dojo/_base/lang",
         "dojo/dom",
         "dojo/dom-style",
         "dojo/fx",
         "dojo/aspect",
         "dojo/Deferred",
         "dojo/request",
         "dojo/topic",
         "dojo/json"], 
         function(
                 array, declare, lang,
                 dom, domStyle, fx,
                 aspect,
                 Deferred, request, topic,
                 JSON) {
   /***************************************************************************
    * zTree common functions
    **************************************************************************/
   ZTreeCommonFunctions = function() {
       var deferred = new Deferred();
       var curStatus = "init", curAsyncCount = 0, asyncForAll = false, goAsync = false;
       check = function() {
           if (curAsyncCount > 0) {
               return false;
           }
           return true;
       };
       expandAll = function(treeId) {
           if (!check()) {
               return;
           }
           var zTree = jQuery.fn.zTree.getZTreeObj(treeId);
           if (asyncForAll) {
               zTree.expandAll(true);
           } else {
               expandNodes(zTree.getNodes(), treeId);
               if (!goAsync) {
                   curStatus = "";
               }
           }
       };
       expandNodes = function(nodes, treeId) {
           if (!nodes) {
               return;
           }
           curStatus = "expand";
           var zTree = jQuery.fn.zTree.getZTreeObj(treeId);
           for ( var i = 0, l = nodes.length; i < l; i++) {
               zTree.expandNode(nodes[i], true, false, false);
               if (nodes[i].isParent && nodes[i].zAsync) {
                   expandNodes(nodes[i].children, treeId);
               } else {
                   goAsync = true;
               }
           }
       };
       asyncAll = function(treeId) {
           if (!check()) {
               return;
           }
           var zTree = jQuery.fn.zTree.getZTreeObj(treeId);
           if (!asyncForAll) {
               asyncNodes(zTree.getNodes(), treeId);
               if (!goAsync) {
                   curStatus = "";
               }
           }
       };
       asyncNodes = function(nodes, treeId) {
           if (!nodes) {
               return;
           }
           curStatus = "async";
           var zTree = jQuery.fn.zTree.getZTreeObj(treeId);
           for ( var i = 0, l = nodes.length; i < l; i++) {
               if (nodes[i].isParent && nodes[i].zAsync) {
                   asyncNodes(nodes[i].children, treeId);
               } else {
                   goAsync = true;
                   zTree.reAsyncChildNodes(nodes[i], "refresh", true);
               }
           }
       };
       beforeAsync = function() {
           curAsyncCount++;
       };
       onAsyncSuccess = function(event, treeId, treeNode, msg) {
           curAsyncCount--;
           if (curStatus == "expand") {
               expandNodes(treeNode.children, treeId);
           } else if (curStatus == "async") {
               asyncNodes(treeNode.children, treeId);
           }
           if (curAsyncCount <= 0) {
               if (curStatus != "init" && curStatus != "") {
                   asyncForAll = true;
                   deferred.resolve("done");
               }
               curStatus = "";
           }
       };
       onAsyncError = function(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
           curAsyncCount--;
           if (curAsyncCount <= 0) {
               curStatus = "";
               if (treeNode != null) {
                   asyncForAll = true;
               }
           }
       };
       return {
           deferred: deferred.promise,
           expandAll: expandAll,
           asyncAll: asyncAll,
           beforeAsync: beforeAsync,
           onAsyncSuccess: onAsyncSuccess,
           onAsyncError: onAsyncError
       };
   };
   
   /********************************************************************************
    * refresh grid or tree subject etc.
    ********************************************************************************/
   var treeTopic = "reload/zTree";
   topic.subscribe(treeTopic, function(treeId) {
       var zTree = jQuery.fn.zTree.getZTreeObj(treeId);
       if(zTree) {
           var nodes = zTree.getSelectedNodes();
           if(nodes.length == 0) {
               zTree.reAsyncChildNodes(null, "refresh");
           }else if(nodes.length == 1) {
               var node = nodes[0];
               zTree.reAsyncChildNodes(node.getParentNode(), "refresh");
           }
       }
   });
   /********************************************************************************
    * Ajax submit to sync database.
    ********************************************************************************/
   syncDB = function(url, postData) {
       request.post(url, {
           headers : { "Content-Type": "application/json; charset=utf-8" },
           data : JSON.stringify(postData.data || postData),
           handleAs : "json",
           timeout : 30000
       }).then(function(response) {
           // 操作提示
           // 刷新数据
       }, function(error) {
       });
   };
});