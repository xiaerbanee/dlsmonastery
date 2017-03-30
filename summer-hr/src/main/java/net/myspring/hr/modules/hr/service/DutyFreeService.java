package net.myspring.hr.modules.hr.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import net.myspring.hr.modules.hr.mapper.DutyFreeMapper;

@Service
public class DutyFreeService {

    @Autowired
    private DutyFreeMapper dutyFreeMapper;

}
