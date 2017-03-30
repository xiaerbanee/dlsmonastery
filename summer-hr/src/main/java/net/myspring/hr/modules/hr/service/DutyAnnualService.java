package net.myspring.hr.modules.hr.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import net.myspring.hr.modules.hr.mapper.DutyAnnualMapper;

@Service
public class DutyAnnualService {

    @Autowired
    private DutyAnnualMapper dutyAnnualMapper;

}
