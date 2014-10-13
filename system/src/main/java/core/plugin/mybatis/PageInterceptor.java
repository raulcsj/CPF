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
package core.plugin.mybatis;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.exception.app.AppRuntimeException;

import core.plugin.mybatis.dialect.Dialect;
import core.plugin.mybatis.dialect.H2Dialect;
import core.plugin.mybatis.dialect.OracleDialect;

/**
 * <p>
 * 通用分页根据不同的数据库实现
 * </p>
 * 
 * @author CSJ (raulcsj@126.com)
 * @version 1.0
 * 
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PageInterceptor implements Interceptor {

    private final static String SQL_SELECT_REGEX = "(?is)^\\s*SELECT.*$";
    private final static String SQL_COUNT_REGEX = "(?is)^\\s*SELECT\\s+COUNT\\s*\\(\\s*(?:\\*|\\w+)\\s*\\).*$";
    private final static Logger L = LoggerFactory.getLogger(PageInterceptor.class);

    @Override
    public Object intercept(Invocation inv) throws Throwable {
        // 拦截到的prepare方法参数是一个Connection对象
        Connection connection = (Connection) inv.getArgs()[0];
        String dbType = connection.getMetaData().getDatabaseProductName();
        L.debug(dbType);
        Dialect dialect = null;
        if (StringUtils.equalsIgnoreCase("ORACLE", dbType)) {
            dialect = new OracleDialect();
        } else if (StringUtils.equalsIgnoreCase("H2", dbType)) {
            dialect = new H2Dialect();
        } else {
            throw new AppRuntimeException("A404: Not Support ['" + dbType + "'] Pagination Yet!");
        }

        StatementHandler target = (StatementHandler) inv.getTarget();
        BoundSql boundSql = target.getBoundSql();
        String sql = boundSql.getSql();
        if (StringUtils.isBlank(sql)) {
            return inv.proceed();
        }
        // 只有为select查询语句时才进行下一步
        if (sql.matches(SQL_SELECT_REGEX) && !Pattern.matches(SQL_COUNT_REGEX, sql)) {
            Object obj = FieldUtils.readField(target, "delegate", true);
            // 反射获取 RowBounds 对象。
            RowBounds rowBounds = (RowBounds) FieldUtils.readField(obj, "rowBounds", true);
            // 分页参数存在且不为默认值时进行分页SQL构造
            if (rowBounds != null && rowBounds != RowBounds.DEFAULT) {
                FieldUtils.writeField(boundSql, "sql", dialect.getSqlWithPagination(sql, rowBounds), true);
                // 一定要还原否则将无法得到下一组数据(第一次的数据被缓存了)
                FieldUtils.writeField(rowBounds, "offset", RowBounds.NO_ROW_OFFSET, true);
                FieldUtils.writeField(rowBounds, "limit", RowBounds.NO_ROW_LIMIT, true);
            }
        }
        return inv.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    public static void main(String[] args) {
        List<String> tests = new ArrayList<String>();
        tests.add("select count(*) from abc \n\t\t where\n abc");
        tests.add("SELECT COUNT(*) from abc");
        tests.add(" select count (*) from abc");
        tests.add(" select count( *) from abc");
        tests.add("select count( * ),id from abc");
        tests.add("select * from abc");
        tests.add("select abc,test,fdas from abc");
        tests.add("select count(adb) from abc");
        tests.add("select count(0) from abc");
        tests.add("select min(count(*)) from abc");
        tests.add("update min(count(*)) from abc");
        tests.add("delete min(count(*)) from abc");
        Pattern p1 = Pattern.compile(SQL_SELECT_REGEX, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Pattern p2 = Pattern.compile(SQL_COUNT_REGEX, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        for (String str : tests) {
            Matcher m1 = p1.matcher(str);
            Matcher m2 = p2.matcher(str);
            System.out.println("匹配字符串: " + str);
            System.out.println(" 是select语句? " + m1.matches());
            System.out.println(" 是count语句? " + m2.matches());
            System.out.println();
        }
    }
}
