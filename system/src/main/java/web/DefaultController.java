/************************************************************************
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 ************************************************************************/
package web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import common.exception.http.NotFoundException;

/**
 * <p>
 * 默认Request请求处理
 * </p>
 * 
 * @author CSJ (raulcsj@126.com)
 * @version 1.0
 */
@Controller
public class DefaultController {
    @RequestMapping(value = { "/", "/home.html" })
    public String home() {
        return "dashboard";
    }

    @RequestMapping("/404.html")
    public void httpError404() {
        throw new NotFoundException("请求未发现!");
    }

    @RequestMapping("/submit.html")
    @ResponseBody
    public void submit(@RequestBody Map<String, String> map) {
        L.info(map.toString());
    }

    private static final Logger L = LoggerFactory.getLogger(DefaultController.class);
}
