package net.myspring.cloud.modules.input.web.query;

import net.myspring.cloud.common.query.BaseQuery;
import net.myspring.cloud.modules.input.dto.NameNumberDto;

import java.util.List;

/**
 * Created by lihx on 2017/5/8.
 */
public class BatchBankDepositJournalQuery extends BaseQuery {
    private String kingdeeName;
    private List<NameNumberDto> accountSubjectList;

    public List<NameNumberDto> getAccountSubjectList() {
        return accountSubjectList;
    }

    public void setAccountSubjectList(List<NameNumberDto> accountSubjectList) {
        this.accountSubjectList = accountSubjectList;
    }

    public String getKingdeeName() {
        return kingdeeName;
    }

    public void setKingdeeName(String kingdeeName) {
        this.kingdeeName = kingdeeName;
    }
}
