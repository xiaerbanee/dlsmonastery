package net.myspring.basic.modules.hr.service;

import net.myspring.basic.modules.hr.mapper.DutyRestOvertimeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DutyRestOvertimeService {

    @Autowired
    private DutyRestOvertimeMapper dutyRestOvertimeMapper;

}
