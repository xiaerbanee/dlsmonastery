package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.modules.hr.domain.DutyWorktime;
import net.myspring.basic.modules.hr.dto.DutyWorktimeDto;
import net.myspring.basic.modules.hr.dto.DutyWorktimeExportDto;
import net.myspring.basic.modules.hr.service.DutyWorktimeService;
import net.myspring.basic.modules.hr.web.form.DutyWorktimeForm;
import net.myspring.basic.modules.hr.web.query.DutyWorktimeQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.time.LocalDateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "hr/dutyWorktime")
public class DutyWorktimeController {

    @Autowired
    private DutyWorktimeService dutyWorktimeService;

    @RequestMapping(value = "getQuery")
    public DutyWorktimeQuery getQuery(DutyWorktimeQuery dutyWorktimeQuery) {
        return dutyWorktimeQuery;
    }

    @RequestMapping(value = "getForm")
    public DutyWorktimeForm getForm(DutyWorktimeForm dutyWorktimeForm){
        return dutyWorktimeForm;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<DutyWorktimeDto> findPage(Pageable pageable, DutyWorktimeQuery dutyWorktimeQuery){
        Page<DutyWorktimeDto> page = dutyWorktimeService.findPage(pageable,dutyWorktimeQuery);
        return page;
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public String export(String month) {
        LocalDate dateStart=LocalDateUtils.getFirstDayOfThisMonth(LocalDate.now());
        if(StringUtils.isNotBlank(month)) {
            String[] yearMonth=StringUtils.split(month, CharConstant.DATE_RANGE_SPLITTER);
            dateStart = LocalDate.of(Integer.valueOf(yearMonth[0]),Integer.valueOf(yearMonth[1]),1);
        }
        LocalDate dateEnd=LocalDateUtils.getLastDayOfThisMonth(dateStart);
        List<DutyWorktimeExportDto> dutyWorktimeExportDtoList=dutyWorktimeService.getDutyWorktimeExportDto(dateStart,dateEnd);
        Workbook workbook = new SXSSFWorkbook(10000);
        return dutyWorktimeService.findSimpleExcelSheet(month,workbook,dutyWorktimeExportDtoList);
    }

    @RequestMapping(value = "import", method = RequestMethod.POST)
    public RestResponse importFile(DutyWorktimeForm dutyWorktimeForm) {
        dutyWorktimeService.save(dutyWorktimeForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

}
