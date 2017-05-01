package net.myspring.cloud.common.handsontable;

import net.myspring.util.text.StringUtils;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by lihx on 2017/4/8.
 */
public class HandSonTableUtils {

    public static String getValue(List<Object> row, Integer index) {
        if (CollectionUtil.isEmpty(row) || ((row.size() - 1) < index)) {
            return "";
        } else {
            return row.get(index).toString().trim();
        }
    }
}
