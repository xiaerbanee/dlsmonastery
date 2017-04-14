package net.myspring.basic.modules.sys.web.controller;

import net.myspring.basic.modules.sys.service.ProcessFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "sys/processFlow")
public class ProcessFlowController {

    @Autowired
    private ProcessFlowService processFlowService;

}
