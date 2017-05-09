package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.utils.SecurityUtils;
import net.myspring.cloud.modules.input.service.BatchBankDepositJournalService;
import net.myspring.cloud.modules.input.web.form.BatchBankDepositJournalForm;
import net.myspring.cloud.modules.input.web.query.BatchBankDepositJournalQuery;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihx on 2017/5/8.
 */
@RestController
@RequestMapping(value = "input/batchBankDepositJournal")
public class BatchBankDepositJournalController {
    @Autowired
    private BatchBankDepositJournalService batchBankDepositJournalService;
    @Autowired
    private KingdeeBookService kingdeeBookService;

    @RequestMapping(value = "form")
    public BatchBankDepositJournalQuery bankForm(BatchBankDepositJournalQuery batchBankDepositJournalQuery) {
        batchBankDepositJournalQuery = batchBankDepositJournalService.getFormProperty(batchBankDepositJournalQuery);
        String kingdeeName = kingdeeBookService.getNameByCompanyId(SecurityUtils.getCompanyId());
        batchBankDepositJournalQuery.setKingdeeName(kingdeeName);
        return batchBankDepositJournalQuery;
    }

    @RequestMapping(value = "save")
    public RestResponse bankSave(BatchBankDepositJournalForm batchBankDepositJournalForm) {
        String data = HtmlUtils.htmlUnescape(batchBankDepositJournalForm.getData());
        List<List<Object>> datas = ObjectMapperUtils.readValue(data, ArrayList.class);
        LocalDate billDate = LocalDateUtils.parse(batchBankDepositJournalForm.getBillDate());
        String subjectNumer = batchBankDepositJournalForm.getAccountSubjectNumer();
        String codeList = batchBankDepositJournalService.saveBankJournal(billDate, subjectNumer, datas);
        return new RestResponse("银行日记账成功：" + codeList,null,true);
    }
}
