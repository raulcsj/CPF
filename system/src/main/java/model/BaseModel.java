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
package model;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

/**
 * <p>
 * 基础数据库可序列化(缓存)模型:
 * 
 * <pre>
 *    添加分页参数, SQL执行statmentId和功能描述LOG日志
 *    封装常用WEB前端UI组件参数
 * </pre>
 * 
 * </p>
 * 
 * @author CSJ (raulcsj@126.com)
 * @version 1.0
 */
@JsonIgnoreType
public class BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    // 对应iBatis dao操作语句id
    private transient String statmentId = "";

    // 功能描述
    private transient String functionDesc = "";

    // 是否分页
    private transient boolean paging = true;

    private transient PagingModel pagingModel = null;

    public BaseModel() {
        pagingModel = new PagingModel();
    }

    public String getFunctionDesc() {
        return functionDesc;
    }

    public void setFunctionDesc(String functionDesc) {
        this.functionDesc = functionDesc;
    }

    public int getLimit() {
        return pagingModel.getLimit();
    }

    public String getOrdering() {
        return pagingModel.getOrdering();
    }

    public int getPage() {
        return pagingModel.getPage();
    }

    public String getRange() {
        return pagingModel.getRange();
    }

    public int getStart() {
        return pagingModel.getStart();
    }

    public String getStatmentId() {
        return statmentId;
    }

    public long getTotalCount() {
        return pagingModel.getTotalCount();
    }

    public boolean isPaging() {
        return paging;
    }

    public void setLimit(int limit) {
        pagingModel.setLimit(limit);
    }

    public void setOrdering(String ordering) {
        pagingModel.setOrdering(ordering);
    }

    public void setPage(int page) {
        pagingModel.setPage(page);
    }

    public void setPaging(boolean paging) {
        this.paging = paging;
    }

    public void setRange(String range) {
        pagingModel.setRange(range);
    }

    public void setSidx(String sidx) {
        pagingModel.setSidx(sidx);
    }

    public void setSord(String sord) {
        pagingModel.setSord(sord);
    }

    public void setStart(int start) {
        pagingModel.setStart(start);
    }

    public void setStatmentId(String statmentId) {
        this.statmentId = statmentId;
    }

    public void setTotalCount(long totalCount) {
        pagingModel.setTotalCount(totalCount);
    }

    public PagingModel getPagingModel() {
        return pagingModel;
    }

    /* 分页模型 */
    @JsonIgnoreType
    class PagingModel {
        // sort(-name):desc | sort(+name):asc
        private static final String SORT_REGX = ".*(sort\\(([-+].+,?)*\\)).*";
        // 分页参数
        private int page = 0;
        private int start = 0;
        private int limit = 0;
        private long totalCount = 0;

        // HTTP-based (RFC 2616) client with RESTful Data 'eg:items=1-10'
        // HTTP HEAD 'Rang or XRange'
        private String range;

        // 按列排序
        private String ordering;
        private String sidx;// 列序号或列名
        private String sord;// 排序方式asc|desc

        public int getLimit() {
            return limit;
        }

        public String getOrdering() {
            return ordering;
        }

        public int getPage() {
            return page;
        }

        public String getRange() {
            return range;
        }

        public String getSidx() {
            return sidx;
        }

        public String getSord() {
            return sord;
        }

        public int getStart() {
            return start;
        }

        public long getTotalCount() {
            return totalCount;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public void setOrdering(String ordering) {
            StringBuilder builder = new StringBuilder();
            if (ordering == null || "".equals(ordering.trim())) {
                if (this.getSidx() != null && !"".equals(this.getSidx())) {
                    builder.append(this.getSidx()).append(" ").append(this.getSord());
                }
                builder.append(",");
            } else {
                Pattern pattern = Pattern.compile(SORT_REGX);
                Matcher matcher = pattern.matcher(ordering);
                if (matcher.lookingAt()) {
                    int count = matcher.groupCount();
                    String querySortStr = matcher.group(count);
                    String[] sorts = querySortStr.split(",");
                    for (String column : sorts) {
                        String order = "asc";
                        if (column.indexOf("-") != -1) {
                            order = "desc";
                        }
                        column = column.replaceFirst("[-+]", "");
                        builder.append(column).append(" ").append(order).append(",");
                    }
                }
            }
            if (builder.indexOf(",") != -1) {
                this.ordering = builder.substring(0, builder.lastIndexOf(",")).trim();
            }
        }

        public void setPage(int page) {
            this.page = page;
        }

        public void setRange(String range) {
            String[] ranges = null;
            int start = 0;
            int limit = 0;
            try {
                ranges = range.split("=")[1].split("-");
                limit = Integer.parseInt(ranges[1]) + 1;
            } catch (Exception e) {
                range = "items=0-99";
                ranges = range.split("=")[1].split("-");
                limit = Integer.parseInt(ranges[1]) + 1;
            }
            start = Integer.parseInt(ranges[0]);

            this.setStart(start);
            this.setLimit(limit);

            this.range = range;
        }

        public void setSidx(String sidx) {
            this.sidx = sidx;
        }

        public void setSord(String sord) {
            this.sord = sord;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public void setTotalCount(long totalCount) {
            this.totalCount = totalCount;
        }

    }
}
