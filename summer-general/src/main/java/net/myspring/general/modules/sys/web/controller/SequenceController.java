package net.myspring.general.modules.sys.web.controller;

import net.myspring.general.modules.sys.service.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "sys/sequence")
public class SequenceController {

    @Autowired
    private SequenceService sequenceService;

    @RequestMapping(value = "nextVal")
    public Long nextVal(String key){
     return sequenceService.nextVal(key);
    }

}
