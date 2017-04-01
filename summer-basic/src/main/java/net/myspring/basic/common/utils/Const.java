package net.myspring.basic.common.utils;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by liuj on 2016-08-20.
 */
public class Const {
    public static final List<String> HR_ACCOUNT_ADMIN_LIST = Lists.newArrayList("1");

    public static final String AMAP_KEY = "6186e993a28a8cba1d5d9e4b8b34efe3";

    public static final String DEFAULT_PASSWORD = "123456";

    public static final int SHIRO_HASH_INTERATIONS = 1024;
    public static final int SHIRO_SALT_SIZE = 8;

    public static final String ROOT_ID = "0";
    public static final String ROOT_PARENT_IDS = "0,";

    public static final String CHAR_SPACE = " ";
    public static final String CHAR_SPACE_FULL = "　";
    public static final String CHAR_COMMA = ",";
    public static final String CHAR_TAB = "	";
    public static final String CHAR_COMMA_FULL = "，";
    public static final String CHAR_ENTER = "\n";
    public static final String CHAR_UNDER_LINE = "_";
    public static final String CHAR_COLON = ":";
    public static final String CHAR_UTF8 = "UTF-8";
    public static final String CHAR_SLASH_LINE = "/";

    public static final Integer OPPO_ORDER_EXPRESS_PRODUCT_QTY=20;
    public static final Integer  VIVO_ORDER_EXPRESS_PRODUCT_QTY=10;
    public static final Integer  LX_ORDER_EXPRESS_PRODUCT_QTY=14;
    public static final Integer  IMOO_ORDER_EXPRESS_PRODUCT_QTY=10;

    public static final String DEPOT_AREA_TYPE_TOWN="乡镇";
    public static final String DEFAULT_AD_PRICESYSTEM="A类物料运费";


    public static final String FORMATTER_DATE = "yyyy-MM-dd";
    public static final String FORMATTER_SHORT_DATE = "yyyyMMdd";
    public static final String FORMATTER_DATE_TIME = "yyyy-MM-dd HH:mm";
    public static final String FORMATTER_LONG_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMATTER_TIME = "HH:mm";
    public static final String FORMATTER_LONG_TIME = "HH:mm:ss";
    public static final String FORMATTER_GOODS_ORDER = "XK"; // 货品订货

    public static final String DATA_SOURCE_TYPE_FUTURE = "FUTURE";
    public static final String DATA_SOURCE_TYPE_LOCAL = "LOCAL";
    public static final Integer DEFAULT_PREIX_LENGTH = 2;

    public static final int DEFAULT_PAGE_SIZE = 5000;

    public static final BigDecimal MIN_AREA_POINT= new BigDecimal("0.01");

    public static final String GROUP_TYPE_OFFICE = "office";
    public static final String GROUP_TYPE_PRODUCT= "product";

    public static final String OFFICE_TYPE_AREA = "100";
    public static final String OFFICE_TYPE_UNIT = "200";

    public static final String REPORT_TYPE_BAOKA = "电子保卡";
    public static final String REPORT_TYPE_SALE = "核销";

    public static final String XCXAUDIT= "xcxtest";


    public static  final String ITEM_ACTION_DETAIL = "详细";
    public static  final String ITEM_ACTION_EDIT = "修改";
    public static  final String ITEM_ACTION_DELETE = "删除";
    public static  final String ITEM_ACTION_AUDIT = "审核";
    public static  final String ITEM_ACTION_BILL = "开单";
    public static  final String ITEM_ACTION_SIGN_IN = "签收";
    public static  final String ITEM_ACTION_SHIP= "发货";
    public static  final String ITEM_ACTION_CARRIER_EDIT="商城订单";
    public static  final String ITEM_ACTION_SRETURN="销售退货";
    public static  final String ITEM_ACTION_SHIP_BACK="重发";
    public static final String ITEM_ACTION_SHIP_PRINT="快递单";
    public static  final String ITEM_ACTION_PASS = "通过";
    public static  final String ITEM_ACTION_NOT_PASS = "不通过";
    public static  final String ITEM_ACTION_INSPECTION = "抽检";
    public static  final String ITEM_ACTION_PRINT = "打印";
    public static  final String ITEM_ACTION_RESET = "重置";
    public static  final String ITEM_ACTION_REPEAT = "重发";
    public static  final String ITEM_ACTION_UPLOAD = "上传";

}
