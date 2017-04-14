package net.myspring.basic.common.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.org.apache.regexp.internal.RE;
import net.myspring.basic.common.dto.NameValueDto;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzm on 2017/4/14.
 */
@Component
public class InitDomainUtils {

    public <P> List<P> initChildIdList(P parentList, Class childClass, List<NameValueDto> nameValueList) {
        return initChildIdList(Lists.newArrayList(parentList), childClass, nameValueList);
    }

    public <P> List<P> initChildIdList(List<P> parentList, Class childClass, List<NameValueDto> nameValueList) {
        if (CollectionUtil.isNotEmpty(nameValueList)) {
            Map<String, List<Object>> map = Maps.newHashMap();
            for (NameValueDto nameValue : nameValueList) {
                if (!map.containsKey(nameValue.getName())) {
                    map.put(nameValue.getName(), Lists.newArrayList());
                }
                map.get(nameValue.getName()).add(nameValue.getValue());
            }
            for (P item : parentList) {
                Object id = ReflectionUtil.getFieldValue(item, "id");
                String childField = WordUtils.uncapitalize(childClass.getSimpleName()) + "IdList";
                ReflectionUtil.setFieldValue(item, childField, map.get(id));
            }
        }
        return parentList;
    }
}
