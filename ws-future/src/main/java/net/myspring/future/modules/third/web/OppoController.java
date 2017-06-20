package net.myspring.future.modules.third.web;


import net.myspring.future.modules.third.service.OppoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "third/factory/oppo")
public class OppoController {
    @Autowired
    private OppoService oppoService;

    @RequestMapping(value="synIme")
    public void synIme(String date){
        oppoService.synIme(date);
    }
}
