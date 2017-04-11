package net.myspring.cloud.modules.sys.web.controller;

import net.myspring.cloud.modules.sys.service.VoucherEntryFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lihx on 2017/4/5.
 */
@RestController
@RequestMapping(value = "sys/glVoucherEntryFlow")
public class VoucherEntryFlowController {
    @Autowired
    private VoucherEntryFlowService voucherEntryFlowService;

}
