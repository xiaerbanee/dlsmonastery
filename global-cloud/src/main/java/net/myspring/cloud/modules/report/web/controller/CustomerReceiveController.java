package net.myspring.cloud.modules.report.web.controller;

import net.myspring.cloud.modules.report.dto.CustomerReceiveDetailDto;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveDetailQuery;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by liuj on 2017/5/11.
 */
@RestController
@RequestMapping(value = "report/customerReceive")
public class CustomerReceiveController {

    @RequestMapping(value = "list")
    public List<CustomerReceiveDto> list(CustomerReceiveQuery customerReceiveQuery) {
        return null;
    }

    @RequestMapping(value = "detail")
    public List<CustomerReceiveDetailDto> list(CustomerReceiveDetailQuery customerReceiveDetailQuery) {
        return null;
    }

}
