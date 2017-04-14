package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.mapper.DemoPhoneTypeOfficeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoPhoneTypeOfficeService {

    @Autowired
    private DemoPhoneTypeOfficeMapper demoPhoneTypeOfficeMapper;

}
