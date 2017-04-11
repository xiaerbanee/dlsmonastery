package net.myspring.cloud.common.enums;

/**
 * Created by lihx on 2017/4/11.
 */
public enum  CharEnum {
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
    CHAR_SLASH_LINE ("/"),
    WAVE_LINE("~");
    private String value;

    CharEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
