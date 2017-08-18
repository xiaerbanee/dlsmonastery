package net.myspring.cloud.modules.report.web.controller;

import net.myspring.cloud.common.enums.KingdeeNameEnum;
import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.report.dto.ConsignmentDto;
import net.myspring.cloud.modules.report.service.ConsignmentWZService;
import net.myspring.cloud.modules.report.web.query.ConsignmentWZQuery;
import net.myspring.common.exception.ServiceException;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 委托代销（温州）
 * Created by lihx on 2017/8/12.
 */
@RestController
@RequestMapping(value = "report/consignmentWZ")
public class ConsignmentWZController {
    @Autowired
    private ConsignmentWZService consignmentWZService;

    @RequestMapping(value = "list")
    public List<ConsignmentDto> list (ConsignmentWZQuery consignmentWZQuery){
        if (KingdeeNameEnum.WZOPPO.name().equals(RequestUtils.getCompanyName())) {
            return consignmentWZService.findConsignmentReport(consignmentWZQuery);
        }else {
            throw new ServiceException("该报表只能用于温州（WZOPPO）账套,请确保当前使用的为温州账套");
        }
    }

    @RequestMapping(value = "export")
    public ModelAndView export (String dateStart, String dateEnd ){
        if (KingdeeNameEnum.WZOPPO.name().equals(RequestUtils.getCompanyName())) {
            if (dateStart != null && dateEnd != null) {
                return new ModelAndView(new ExcelView(), "simpleExcelBook", consignmentWZService.export(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd)));
            }else{
                throw new ServiceException("时间不能为空！");
            }
        }else {
            throw new ServiceException("该报表只能用于温州（WZOPPO）账套,请确保当前使用的为温州账套");
        }
    }
}
