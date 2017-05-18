package net.myspring.future.modules.crm.web.controller;

import net.myspring.future.modules.crm.dto.StoreAllotDetailDto;
import net.myspring.future.modules.crm.service.StoreAllotDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "crm/storeAllotDetail")
public class StoreAllotDetailController {

    @Autowired
    private StoreAllotDetailService storeAllotDetailService;



    @RequestMapping(value = "getDetails")
    public List<StoreAllotDetailDto> getDetails(String storeAllotId) {
        return storeAllotDetailService.findByStoreAllotId(storeAllotId);
    }


}
