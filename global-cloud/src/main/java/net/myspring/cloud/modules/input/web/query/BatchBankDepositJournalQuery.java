package net.myspring.cloud.modules.input.web.query;

import net.myspring.cloud.modules.input.dto.NameNumberDto;

import java.util.List;

/**
 * Created by lihx on 2017/5/8.
 */
public class BatchBankDepositJournalQuery {
    private String kingdeeName;
    private List<NameNumberDto> accountSubject;

    public List<NameNumberDto> getAccountSubject() {
        return accountSubject;
    }

    public void setAccountSubject(List<NameNumberDto> accountSubject) {
        this.accountSubject = accountSubject;
    }

    public String getKingdeeName() {
        return kingdeeName;
    }

    public void setKingdeeName(String kingdeeName) {
        this.kingdeeName = kingdeeName;
    }
}
