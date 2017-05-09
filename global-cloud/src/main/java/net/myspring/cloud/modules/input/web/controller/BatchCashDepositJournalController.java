package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.service.BatchCashDepositJournalService;
import net.myspring.cloud.modules.input.web.form.BatchCashDepositJournalForm;
import net.myspring.cloud.modules.input.web.query.BatchCashDepositJournalQuery;
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
@RequestMapping(value = "input/batchCashDepositJournal")
public class BatchCashDepositJournalController {
    @Autowired
    private BatchCashDepositJournalService batchCashDepositJournalService;
    @Autowired
    private KingdeeBookService kingdeeBookService;

    @RequestMapping(value = "form")
    public BatchCashDepositJournalQuery cashForm(BatchCashDepositJournalQuery batchCashDepositJournalQuery) {
        String kingdeeName = kingdeeBookService.getNameByCompanyId(RequestUtils.getCompanyId());
        batchCashDepositJournalQuery.setKingdeeName(kingdeeName);
        return batchCashDepositJournalQuery;
    }

    @RequestMapping(value = "save")
    public RestResponse cashSave(BatchCashDepositJournalForm batchCashDepositJournalForm) {
        String data = HtmlUtils.htmlUnescape(batchCashDepositJournalForm.getData());
        List<List<Object>> datas = ObjectMapperUtils.readValue(data, ArrayList.class);
        LocalDate date = LocalDateUtils.parse(batchCashDepositJournalForm.getBillDate());
        String codeList = batchCashDepositJournalService.saveCashJournal(date, datas);
        return new RestResponse("现金日记账成功：" + codeList,null,true);
    }

}
