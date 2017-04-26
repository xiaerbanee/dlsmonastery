package net.myspring.cloud.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by lihx on 2016/12/31.
 */
public enum ConsignmentReportHeadEnum {

    CUSTOMER_CODE("客户代码"),
    CUSTOMER_NAME("客户名称"),
    GOODS_CODE("商品代码"),
    GOODS_NAME("商品名称"),
    CONSIGNMENT_INITIAL_QUANTITY("寄售期初数量"),
    CONSIGNMENT_INITIAL_PRICE("寄售期初单价"),
    CONSIGNMENT_INITIAL_AMOUNT("寄售期初金额"),
    CONSIGNMENT_SEND_QUANTITY("寄售发出数量"),
    CONSIGNMENT_SEND_PRICE("寄售发出单价"),
    CONSIGNMENT_SEND_AMOUNT("寄售发出金额"),
    CONSIGNMENT_SETTLEMENT_QUANTITY("寄售结算数量"),
    CONSIGNMENT_SETTLEMENT_PRICE("寄售结算单价"),
    CONSIGNMENT_SETTLEMENT_AMOUNT("寄售结算金额"),
    CONSIGNMENT_NOT_SETTLEMENT_QUANTITY("寄售未结算数量"),
    CONSIGNMENT_NOT_SETTLEMENT_PRICE("寄售未结算单价"),
    CONSIGNMENT_NOT_SETTLEMENT_AMOUNT("寄售未结算金额");

    private static List<String> names = Lists.newArrayList();

    private String name;

    ConsignmentReportHeadEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<String> getNames() {
        if (CollectionUtil.isEmpty(names)) {
            for (ConsignmentReportHeadEnum consignmentReportEnum : ConsignmentReportHeadEnum.values()) {
                names.add(consignmentReportEnum.getName());
            }
        }
        return names;
    }
}
