package net.myspring.future.modules.basic.service;

import net.myspring.future.modules.basic.repository.DemoPhoneTypeOfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoPhoneTypeOfficeService {

    @Autowired
    private DemoPhoneTypeOfficeRepository demoPhoneTypeOfficeRepository;

}
