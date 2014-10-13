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
package dao;

/**
 * <p>
 * MyBatis-Mapper 统一接口:
 * 
 * <pre>
 *   所有具体实体Mapper中XML statement id对应方法名
 *   package org.mybatis;
 *   public interface UserDao extends BaseMapper {
 *      public User queryUserById(User user);
 *   }
 *   <mapper namespace="org.mybatis.UserDao">
 *     <select id="queryUserById" resultType="User">
 *         select * from t_user;
 *     </select>
 *   </mapper>
 * </pre>
 * 
 * </p>
 *
 * @author CSJ (raulcsj@126.com)
 * @version 1.0
 */
public interface BaseMapper {
}
