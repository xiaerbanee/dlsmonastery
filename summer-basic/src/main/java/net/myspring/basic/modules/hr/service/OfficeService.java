package net.myspring.basic.modules.hr.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import net.myspring.basic.modules.hr.mapper.OfficeMapper;

@Service
public class OfficeService {

    @Autowired
    private OfficeMapper officeMapper;

}
