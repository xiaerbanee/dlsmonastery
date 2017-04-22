package net.myspring.future.common.utils;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by liuj on 2016-08-20.
 */
public class Const {
    public static final List<String> HR_ACCOUNT_ADMIN_LIST = Lists.newArrayList("1");

    public static final String DEFAULT_PASSWORD = "123456";

    public static final String ROOT_PARENT_IDS = "0,";

    public static final String CHAR_SPACE = " ";
    public static final String CHAR_SPACE_FULL = "　";
    public static final String CHAR_COMMA = ",";
    public static final String CHAR_ENTER = "\n";
    public static final String CHAR_COLON = ":";
    public static final String CHAR_SLASH_LINE = "/";

    public static final int DEFAULT_PAGE_SIZE = 5000;
    public static final Integer DEFAULT_PREIX_LENGTH = 2;

    public static final String XCXAUDIT= "xcxtest";

    public static final BigDecimal MIN_AREA_POINT= new BigDecimal("0.01");

    public static final String GROUP_TYPE_OFFICE = "office";
    public static final String GROUP_TYPE_PRODUCT= "product";

    public static final String OFFICE_TYPE_AREA = "100";
    public static final String OFFICE_TYPE_UNIT = "200";

}
