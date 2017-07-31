package net.myspring.future.modules.basic.web.controller;

import net.myspring.future.modules.basic.dto.SimpleProcessDetailDto;
import net.myspring.future.modules.basic.service.SimpleProcessService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "basic/simpleProcess")
public class SimpleProcessController {

    @Autowired
    private SimpleProcessService processDetailService;

    @RequestMapping(value = "findBySimpleProcessId")
    public List<SimpleProcessDetailDto> findBySimpleProcessId(String simpleProcessId){
        if(StringUtils.isBlank(simpleProcessId)){
            return new ArrayList<>();
        }
        return processDetailService.findBySimpleProcessId(simpleProcessId);
    }
}
