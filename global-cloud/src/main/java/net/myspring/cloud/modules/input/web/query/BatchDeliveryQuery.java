package net.myspring.cloud.modules.input.web.query;

import net.myspring.cloud.common.enums.MisDeliveryTypeEnum;


/**
 * Created by lihx on 2017/5/8.
 */
public class BatchDeliveryQuery {
    private MisDeliveryTypeEnum[] types;

    public MisDeliveryTypeEnum[] getTypes() {
        return types;
    }

    public void setTypes(MisDeliveryTypeEnum[] types) {
        this.types = types;
    }
}
