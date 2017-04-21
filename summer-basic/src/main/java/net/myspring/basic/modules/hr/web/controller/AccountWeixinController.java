package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.config.ExcelView;
import net.myspring.basic.common.enums.BoolEnum;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.dto.AccountDto;
import net.myspring.basic.modules.hr.dto.AccountWeixinDto;
import net.myspring.basic.modules.hr.service.AccountService;
import net.myspring.basic.modules.hr.service.AccountWeixinService;
import net.myspring.basic.modules.hr.service.PositionService;
import net.myspring.basic.modules.hr.web.form.AccountForm;
import net.myspring.basic.modules.hr.web.query.AccountQuery;
import net.myspring.basic.modules.sys.web.form.WeixinAccountForm;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelSheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/3/19.
 */
@RestController
@RequestMapping(value = "hr/accountWeixin")
public class AccountWeixinController {

    @Autowired
    private AccountWeixinService accountWeixinService;

    @RequestMapping(value = "findByAccountId")
    public AccountWeixinDto findByAccountId(String accountId) {
        return accountWeixinService.findByAccountId(accountId);
    }
}
