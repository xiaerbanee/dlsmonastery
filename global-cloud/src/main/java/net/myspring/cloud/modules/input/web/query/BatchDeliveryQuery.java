package net.myspring.cloud.modules.input.web.query;

import net.myspring.cloud.common.enums.MisDeliveryTypeEnum;
import net.myspring.cloud.common.query.BaseQuery;


/**
 * Created by lihx on 2017/5/8.
 */
public class BatchDeliveryQuery extends BaseQuery {
    private MisDeliveryTypeEnum[] types;

    public MisDeliveryTypeEnum[] getTypes() {
        return types;
    }

    public void setTypes(MisDeliveryTypeEnum[] types) {
        this.types = types;
    }
}
