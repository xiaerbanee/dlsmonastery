package net.myspring.general.modules.sys.web.controller;

import net.myspring.general.modules.sys.service.ProcessFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "sys/processFlow")
public class ProcessFlowController {

    @Autowired
    private ProcessFlowService processFlowService;



}
