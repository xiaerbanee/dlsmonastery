package net.myspring.tool.common.utils;
import com.google.common.collect.Maps;
import net.myspring.util.text.MD5Utils;
import org.springframework.util.Base64Utils;

import java.util.Map;
public class Const {
    public static final String FUTURE_LOCAL= System.getenv("FUTURE_LOCAL");

    public static final String DEFULT_ACCOUNT_ID ="1";
    //移动账号信息
    public static final String CMCC_OPPO_USERNAME="ncop";
    public static final String CMCC_VIVO_USERNAME="ksdz";
    public static final String CMCC_PASSWORD= Base64Utils.encodeToString(MD5Utils.encode("qwerzxcv").getBytes());


    //联信账户信息
    public static final String CTCC_OPPO_USERNAME="mj17707992222";
    public static final String CTCC_OPPO_PASSWORD="oppo2416";

    public static final String CTCC_VIVO_USERNAME="mj18172927777";
    public static final String CTCC_VIVO_PASSWORD="vivo3458";

    public static final String CTCC_OPPO_PROXY_DEPOT_ID=",153,621,141,350,175,251,162,2062,220,";
    public static final String CTCC_VIVO_PROXY_DEPOT_ID=",1075,1076,1073,1074,1071,1077,1078,1072,";

    //k3cloud参数
    public static final String K3Cloud_POST_URL = "ID".equals(Const.FUTURE_LOCAL)?"http://127.0.0.1/K3Cloud/":"http://168.192.1.13/K3Cloud/";

    public static final Integer K3Cloud_LANG = 2052;

    public static final int  CMCC_B2B_TASK_SIZE = 20;
    public static final String FORMATTER_GOODS_ORDER = "XK";
    public static final String CHAR_ENTER = "\n";
    public static final String CHAR_COMMA = ",";
    public static final String CHAR_SEMICOLON = ";";
    public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_TIMEZONE = "GMT+8";

    public static final String BUSINESS_TYPE_RETAIL = "retail";
    public static final String BUSINESS_TYPE_FUTURE = "future";
    public static final String BUSINESS_TYPE_LEDGER = "ledger";

    public static final String TOTAL_DEPARTMENT = "合计";
    public static final String ACCUMULATE = "累计";
    public static final String SUBJECT_NAME = "科目名称";
    public static final String AMOUNT = "金额";
    public static final String PROPORTION = "占比";

    public static final String ROOT_ID = "0";
    public static final String ROOT_PARENT_IDS = "0,";
    public static final String DOUBLE_ZERO = "00";
    public static final String JX_FACTORY_AGENT_CODES ="M13E00,M13D00,M13H00,M13I00,M13A00,M13C00,M13G00";
    public static final String JX_FACTORY_AGENT_NAMES ="南昌凯胜,南昌小客户,南昌宝瑞电子有限公司,南昌汇信电子有限公司,通联,伟特,江西小客户";
    public static final String ID_FACTORY_AGENT_CODES ="R250082";
    public static final String ID_FACTORY_AGENT_NAMES ="印尼江西";

    public enum CompanyName{
        JXTDZB,
        WZOPPO,
        JXOPPO,
        JXVIVO,
        ZHMC;
    }

    public enum CompanyConfig{
        JXOPPO_FACTORY_AGENT_CODES ("M13AMB,M13CMB,M13DMB,M13EMB,M13FMB,M13GMB,M13HMB,M13JMB,M13KMB"),
        JXOPPO_FACTORY_AGENT_PASSWORD ("OP098,OP098,OP098,OP098,OP098,OP098,OP098,OP098,OP098"),
        JXOPPO_LX_FACTORY_AGENT_CODES ("M13HMB,M13JMB,M13KMB"),
        JXOPPO_LX_DEFAULT_STORE_ID("12153"),
        JXOPPO_DEFAULT_STORE_ID("3489"),
        JXOPPO_GOOD_STORE_ID("3490"),

        WZOPPO_FACTORY_AGENT_CODES ("M03FMB,M03GMB,M03RMB,M03SMB,M03UMB,M03YMB"),
        WZOPPO_FACTORY_AGENT_PASSWORD ("OP098,OP098,OP098,OP098,OP098,OP098"),
        WZOPPO_DEFAULT_STORE_ID("20624"),
        WZOPPO_GOOD_STORE_ID("20643"),

        JXVIVO_FACTORY_AGENT_CODES ("M13E00,M13D00,M13H00,M13I00,M13A00,M13C00,M13G00"),
        JXVIVO_LX_FACTORY_AGENT_CODES ("M13A00,M13C00,M13G00"),
        JXVIVO_LX_DEFAULT_STORE_ID("11344"),
        JXVIVO_DEFAULT_STORE_ID("3489"),
        JXVIVO_GOOD_STORE_ID("3490"),

        IDVIVO_FACTORY_AGENT_CODES ("R250082,R2500821,R2500822,R2500823"),
        IDVIVO_DEFAULT_STORE_ID("1"),
        IDVIVO_GOOD_STORE_ID("1"),

        JXIMOO_FACTORY_AGENT_CODES ("M13A03"),
        JXIMOO_DEFAULT_STORE_ID("1");

        private String value;
        private static Map<String, String> map;

        CompanyConfig(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Map<String, String> getMap() {
            if (map == null) {
                map = Maps.newLinkedHashMap();
                for (CompanyConfig companyConfig : CompanyConfig.values()) {
                    map.put(companyConfig.name(), companyConfig.getValue());
                }
            }
            return map;
        }
    }

    public enum DatabaseType {
        MYSQL,SQLSERVER,ORACLE
    }

    public enum CharEnum {
        BR("<br/>"),
        VERTICAL_LINE("|"),
        UNDER_LINE("_"),
        TILDE("_"),
        SPACE(" "),
        ENTER("\n"),
        COMMA(","),
        COLON(":"),
        SPACE_FULL("　"),
        MINUS("-"),
        UTF8("UTF-8"),
        WAVE_LINE("~");
        private String value;

        CharEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum DateFormat {
        DATE("yyyy-MM-dd"),
        DATE_SHORT("yyyyMMdd"),
        DATE_TIME("yyyy-MM-dd HH:mm:ss"),
        MONTH("yyyy-MM"),
        MONTH_SINGLE("yyyy.M"),
        TIME("HH:mm:ss");
        private String value;

        DateFormat(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum CookieEnum {
        SIDEBAR_CLASS("sidebarClass");
        private String value;

        CookieEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum BoolEnum {
        TRUE("是", true),
        FALSE("否", false);
        private static Map<Boolean, String> map;

        BoolEnum(String name, Boolean value) {
            this.name = name;
            this.value = value;
        }

        private String name;
        private Boolean value;

        public String getName() {
            return name;
        }

        public Boolean getValue() {
            return value;
        }

        public static Map<Boolean, String> getMap() {
            if (map == null) {
                map = Maps.newLinkedHashMap();
                for (BoolEnum bool : BoolEnum.values()) {
                    map.put(bool.getValue(), bool.getName());
                }
            }
            return map;
        }
    }


}
