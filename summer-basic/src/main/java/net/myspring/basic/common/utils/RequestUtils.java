package net.myspring.basic.common.utils;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.SearchEntity;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2016-08-24.
 */
public class RequestUtils {

    public static SearchEntity getSearchEntity(HttpServletRequest request) {
        Enumeration paramNames = request.getParameterNames();
        //Default pageable
        int pageNumber =0;
        int pageSize = 25;
        List<String> orderList = Lists.newArrayList("id:DESC");
        Map<String,Object> params = Maps.newHashMap();
        while ((paramNames != null) && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues != null && paramValues.length > 0) {
                String firstParamValue = StringUtils.isBlank(paramValues[0])?"":paramValues[0].trim();
                switch (paramName) {
                    case "pageNumber":
                        if(StringUtils.isNotBlank(firstParamValue)) {
                            pageNumber = Integer.valueOf(firstParamValue);
                        }
                        break;
                    case "pageSize":
                        if(StringUtils.isNotBlank(firstParamValue)) {
                            pageSize = Integer.valueOf(firstParamValue);
                        }
                        break;
                    case "order":
                        if(CollectionUtil.isNotEmpty(getValues(paramValues))) {
                            orderList = getValues(paramValues);
                        }
                        break;
                    default:
                        if(paramValues.length==1) {
                            //如果paramName末尾是dateEnd，将paramValue自动加23:59:59
                            if(StringUtils.isNotBlank(firstParamValue)) {
                                if(paramName.toLowerCase().endsWith("dateend")){
                                    firstParamValue = firstParamValue + " 23:59:59";
                                } else if (paramName.toLowerCase().endsWith("datestart")){
                                    firstParamValue = firstParamValue + " 00:00:00";
                                }
                            }
                            if(StringUtils.isNotBlank(firstParamValue)) {
                                params.put(paramName,firstParamValue);
                            }
                            //如果是时间段，分开
                            if(paramName.endsWith("BTW") && StringUtils.isNotBlank(firstParamValue)) {
                                String tempParamName = paramName.substring(0,paramName.lastIndexOf("BTW"));
                                String[] tempParamValues = firstParamValue.split(" - ");
                                params.put(tempParamName + "Start",tempParamValues[0]);
                                params.put(tempParamName + "End",tempParamValues[1] + " 23:59:59");
                            }
                        } else {
                            List<String> values = getValues(paramValues);
                            if(CollectionUtil.isNotEmpty(values)) {
                                params.put(paramName,values);
                            }
                        }
                        break;
                }
            }
        }
        //如果从页面搜索，将搜索条件保存到缓存
        List<Sort.Order> orders = Lists.newArrayList();
        for(String order:orderList) {
            String direction = order.split(Const.CHAR_COLON)[1];
            String property = order.split(Const.CHAR_COLON)[0];
            property = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE,property);
            Sort.Order sortOrder;
            if(Sort.Direction.ASC.name().equals(direction) || "ascending".equals(direction)) {
                sortOrder = new Sort.Order(Sort.Direction.ASC,property);
            } else {
                sortOrder = new Sort.Order(Sort.Direction.DESC,property);
            }
            orders.add(sortOrder);
        }
        Pageable pageable = new PageRequest(pageNumber,pageSize,new Sort(orders));
        SearchEntity searchEntity = new SearchEntity();
        searchEntity.setPageable(pageable);
        searchEntity.setParams(params);
        return searchEntity;
    }

    public static Boolean isAjax(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equalsIgnoreCase(header)) {
            return true;
        } else {
            return false;
        }
    }

    private static List<String> getValues(String[] paramValues) {
        List<String> values = Lists.newArrayList();
        for(String paramValue:paramValues) {
            if(StringUtils.isNotBlank(paramValue)) {
                values.add(paramValue.trim());
            }
        }
        return values;
    }
}
