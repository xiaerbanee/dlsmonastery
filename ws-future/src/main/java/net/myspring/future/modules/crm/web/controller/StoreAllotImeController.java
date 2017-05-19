package net.myspring.future.modules.crm.web.controller;

import net.myspring.future.modules.crm.dto.StoreAllotImeDto;
import net.myspring.future.modules.crm.service.StoreAllotImeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "crm/storeAllotIme")
public class StoreAllotImeController {

    @Autowired
    private StoreAllotImeService storeAllotImeService;

    @RequestMapping(value = "getImes")
    public List<StoreAllotImeDto> getImes(String storeAllotId) {
        return storeAllotImeService.findByStoreAllotId(storeAllotId);
    }

}
