package net.myspring.general.modules.sys.web.controller;

import net.myspring.general.modules.sys.service.ProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "sys/processType")
public class ProcessTypeController {

    @Autowired
    private ProcessTypeService processTypeService;
}
