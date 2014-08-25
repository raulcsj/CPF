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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import common.exception.http.NotFoundException;

/**
 * @author CSJ
 * @email raulcsj@126.com
 * @create 2014年8月21日
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
}
