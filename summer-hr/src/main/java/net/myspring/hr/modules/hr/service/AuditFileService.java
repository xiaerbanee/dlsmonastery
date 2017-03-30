package net.myspring.hr.modules.hr.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import net.myspring.hr.modules.hr.mapper.AuditFileMapper;

@Service
public class AuditFileService {

    @Autowired
    private AuditFileMapper auditFileMapper;

}
