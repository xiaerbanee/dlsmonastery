package net.myspring.hr.modules.hr.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import net.myspring.hr.modules.hr.mapper.DutyWorktimeMapper;

@Service
public class DutyWorktimeService {

    @Autowired
    private DutyWorktimeMapper dutyWorktimeMapper;

}
