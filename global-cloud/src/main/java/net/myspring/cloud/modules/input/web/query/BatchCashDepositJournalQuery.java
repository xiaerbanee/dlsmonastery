package net.myspring.cloud.modules.input.web.query;

import net.myspring.cloud.common.query.BaseQuery;

/**
 * Created by lihx on 2017/5/8.
 */
public class BatchCashDepositJournalQuery extends BaseQuery {
    private String kingdeeName;

    public String getKingdeeName() {
        return kingdeeName;
    }

    public void setKingdeeName(String kingdeeName) {
        this.kingdeeName = kingdeeName;
    }
}
