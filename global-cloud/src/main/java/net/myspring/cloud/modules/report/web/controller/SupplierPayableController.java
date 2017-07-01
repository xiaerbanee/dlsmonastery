package net.myspring.cloud.modules.report.web.controller;

import net.myspring.cloud.modules.report.dto.SupplierPayableDetailDto;
import net.myspring.cloud.modules.report.dto.SupplierPayableDto;
import net.myspring.cloud.modules.report.service.SupplierPayableService;
import net.myspring.cloud.modules.report.web.query.SupplierPayableDetailQuery;
import net.myspring.cloud.modules.report.web.query.SupplierPayableQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  供应商-应付
 * Created by liuj on 2017/5/11.
 */
@RestController
@RequestMapping(value = "report/supplierPayable")
public class SupplierPayableController {
    @Autowired
    private SupplierPayableService supplierPayableService;

    @RequestMapping(value = "list",method = RequestMethod.GET)
    public List<SupplierPayableDto> list(SupplierPayableQuery supplierPayableQuery) {
        List<SupplierPayableDto> supplierPayableDtoList =  supplierPayableService.findSupplierPayableDtoList(supplierPayableQuery);
        return supplierPayableDtoList;
    }


    @RequestMapping(value = "detail",method = RequestMethod.GET)
    public List<SupplierPayableDetailDto> detail(SupplierPayableDetailQuery supplierPayableDetailQuery) {
        return supplierPayableService.findSupplierPayableDetailDtoList(supplierPayableDetailQuery);
    }


    @RequestMapping(value = "getQuery")
    public SupplierPayableQuery getQuery() {
        return supplierPayableService.getQuery();
    }

}
