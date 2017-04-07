package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.config.ExcelView;
import net.myspring.basic.common.enums.BoolEnum;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.dto.AccountDto;
import net.myspring.basic.modules.hr.service.AccountService;
import net.myspring.basic.modules.hr.service.PositionService;
import net.myspring.basic.modules.hr.web.form.AccountForm;
import net.myspring.basic.modules.hr.web.query.AccountQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.json.ObjectMapperUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
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
@RequestMapping(value = "account")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private PositionService positionService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Pageable pageable, AccountQuery accountQuery){
        Page<AccountDto> page = accountService.findPage(pageable,accountQuery);
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "delete")
    public String delete(String id) {
        accountService.logicDeleteOne(id);
        RestResponse restResponse =new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "save")
    public String save(AccountForm accountForm) {
        accountService.save(accountForm);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功",ResponseCodeEnum.saved.name()));
    }

    @RequestMapping(value = "findOne")
    public String findOne(Account account){
        return ObjectMapperUtils.writeValueAsString(account);
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("position",positionService.findAll());
        map.put("bools", BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="getListProperty")
    public String getListProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("position",positionService.findAll());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "search")
    public String search(String type,String key){
        Map<String,Object> map=Maps.newHashMap();
        map.put("name",key);
        map.put("type",type);
        List<AccountDto> accountDtoList=accountService.findByLoginNameLikeAndType(map);
        return ObjectMapperUtils.writeValueAsString(accountDtoList);
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export(AccountQuery accountQuery) {
        ExcelView excelView = new ExcelView();
        Workbook workbook = new SXSSFWorkbook(Const.DEFAULT_PAGE_SIZE);
        List<SimpleExcelSheet> simpleExcelSheetList=accountService.findSimpleExcelSheets(workbook,accountQuery);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"账户信息.xlsx",simpleExcelSheetList);
        return new ModelAndView(excelView, "simpleExcelBook", simpleExcelBook);
    }

}
